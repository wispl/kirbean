package me.wisp.kirbean.audio.tracks;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackQueue {
    private final BlockingQueue<AudioTrack> playingQueue;
    // keep a copy of the original queue to revert in case of shuffling
    private final CopyOnWriteArrayList<AudioTrack> cachedQueue;
    private boolean loop;
    private boolean shuffled;

    public TrackQueue() {
        this.playingQueue = new LinkedBlockingQueue<>();
        this.cachedQueue = new CopyOnWriteArrayList<>();
    }

    public void add(AudioTrack track) {
        playingQueue.offer(track);
    }

    public AudioTrack provideTrack() {
        if (loop) {
            return playingQueue.peek().makeClone();
        }
        return playingQueue.poll();
    }

    public List<String> getQueue() {
        List<String> queue = new LinkedList<>();
        StringBuilder stringBuilder = new StringBuilder();

        int count = 1;
        for (AudioTrack track: playingQueue) {
            stringBuilder
                    .append("```\n")
                    .append(count)
                    .append(": ")
                    .append(track.getInfo().title)
                    .append("\n```");

            if (count % 15 == 0 || count >= playingQueue.size()) {
                queue.add(stringBuilder.toString());
                stringBuilder.setLength(0);
                stringBuilder.trimToSize();
            }
            count++;
        }

        return queue;
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
