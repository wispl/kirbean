package me.wisp.kirbean.framework.interactivity;

import java.util.concurrent.CompletableFuture;

public class ExpiringKey {
    private final Interactive interactive;
    private final Runnable onEnd;
    private final long timeout;
    private long lastInteraction;


    public ExpiringKey(Interactive interactive, long timeout, Runnable onEnd) {
        this.interactive = interactive;
        this.timeout = timeout;
        this.onEnd = onEnd;
        lastInteraction = System.currentTimeMillis();
    }

    public Interactive getInteractive() {
        return interactive;
    }

    public long getLastInteraction() {
        return lastInteraction;
    }

    public boolean isExpired() {
        CompletableFuture<String> t = new CompletableFuture<>();
        return System.currentTimeMillis() - lastInteraction >= timeout;
    }

    public void renew() {
        lastInteraction = System.currentTimeMillis();
    }

    public void end() {
        onEnd.run();
    }
}
