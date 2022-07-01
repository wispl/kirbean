package me.wisp.kirbean.interactivity;

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

    public long isExpired() {
        return lastInteraction;
    }

    public void renew() {
        lastInteraction = System.currentTimeMillis();
    }

    public void end() {
        onEnd.run();
    }
}
