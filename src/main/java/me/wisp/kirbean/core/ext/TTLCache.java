package me.wisp.kirbean.core.ext;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A simple unbounded cache which expires entries based on access time.
 * The cache is ordered base on a LRU algorithm. A periodic maintenance
 * is carried out during write operations;
 *
 * This class is thread-safe via reenentrant locks.
 * @param <K>  key type of cache
 * @param <V>  value type of cache
 */
public class TTLCache<K, V> {
    // default timeout of 300_000
    private static final long DEFAULT_TTL = 300_000;

    private final HashMap<K, Node<V>> map = new HashMap<>();
    private Node<V> head;
    private Node<V> tail;

    private final AtomicBoolean isCleaning = new AtomicBoolean();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Gets a value from the cache and refreshes it TTL
     * @param key fetches the node assigned to this key
     * @return the value held by the node
     */
    public V get(K key) {
        lock.readLock().lock();
        try {
            final var node = map.get(key);
            if (node != null && !node.isExpired()) {
                promote(node);
                return node.get();
            }
            map.remove(key);
            removeNode(node);
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void put(K key, V value) {
        put(key, value, DEFAULT_TTL);
    }

    /**
     * Puts a key into the cache, if it exists already, refresh its access time.
     * Additionally perform a cleanup task
     * @param key key to assign value to
     * @param value value to assign to key
     * @param timeout timeout after access, the default TTL is 5 minutes
     */
    public void put(K key, V value, long timeout) {
        // check if the node already exists
        lock.writeLock().lock();
        try {
            final var node = map.get(key);
            if (node != null) {
                // TODO: Should we really promote the node?
                promote(node);
            }

            final var new_node = new Node<V>(value, timeout);
            map.put(key, new_node);
            linkTail(new_node);
        } finally {
            lock.writeLock().unlock();
        }

        // TODO: Maybe enforce minimum size for cleanup and examine affects
        //       of congetsion for large ttl
        if (!isCleaning.get()) {
            isCleaning.set(true);
            cleanup();
            isCleaning.set(false);
        }
    }

    private void promote(Node<V> node) {
        removeNode(node);
        linkHead(node);
    }

    private void linkTail(Node<V> node) {
        var tmp = tail;
        tail = node;

        if (tmp == null) {
            head = node;
        } else {
            tail.prev = tmp;
            tmp.next = tail;
        }
    }

    private void linkHead(Node<V> node) {
        var tmp = head;
        head = node;

        if (tmp != null) {
            head.next = tmp;
            tmp.prev = head;
        }
    }

    private void removeNode(Node<V> node) {
        if (node == tail) {
            tail = node.prev;
            tail.next = null;
        } else if (node == head) {
            head = node.next;
            head.prev = null;
        } else {
            var prevNode = node.prev;
            var nextNode = node.next;
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }
    }

    private void cleanup() {
        lock.writeLock().lock();
        try {
            while (tail != null && tail.isExpired()) {
                removeNode(tail);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private static final class Node<V> {
        private final long ttl;
        private final V value;
        private long lastAccessed;

        public Node<V> prev, next;

        Node(V value, long ttl) {
            this.value = value;
            this.ttl = ttl;
            lastAccessed = System.currentTimeMillis();
        }

        V get() {
            this.lastAccessed = System.currentTimeMillis();
            return value;
        }

        boolean isExpired() {
            return (System.currentTimeMillis() - lastAccessed) > ttl;
        }
    }
}
