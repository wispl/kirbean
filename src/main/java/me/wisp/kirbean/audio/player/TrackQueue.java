package me.wisp.kirbean.audio.player;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackQueue {
    private final Queue<AudioTrack> playingQueue = new LinkedBlockingQueue<>();
    // keep a copy of the original queue to revert in case of shuffling
    private final CopyOnWriteArrayList<AudioTrack> cachedQueue = new CopyOnWriteArrayList<>();
    private boolean loop;
    private boolean shuffled;

    public void add(AudioTrack track) {
        playingQueue.offer(track);
    }

    public AudioTrack provideTrack() {
        if (loop) {
            if (playingQueue.peek() != null) {
                return playingQueue.peek().makeClone();
            }
        }
        return playingQueue.poll();
    }

    public Queue<AudioTrack> getQueue() {
        return playingQueue;
    }

    public boolean isEmpty() {
        return playingQueue.isEmpty();
    }

    public void toggleLoop() {
        loop = !loop;
    }

    public boolean getLoop() {
        return loop;
    }

    public void shuffle() {
        shuffled = true;
        cachedQueue.addAllAbsent(playingQueue);
        List<AudioTrack> shuffledList = new ArrayList<>(playingQueue);
        Collections.shuffle(shuffledList);

        setTo(shuffledList);
    }

    public void unShuffle() {
        shuffled = false;
        cachedQueue.addAllAbsent(playingQueue);
        cachedQueue.retainAll(playingQueue);

        setTo(cachedQueue);
    }

    public void setTo(Collection<? extends AudioTrack> collection) {
        clear();
        playingQueue.addAll(collection);
    }

    public void clear() {
        playingQueue.clear();
    }

    public boolean isShuffled() {
        return shuffled;
    }
}
