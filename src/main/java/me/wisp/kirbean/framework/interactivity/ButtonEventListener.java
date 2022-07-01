package me.wisp.kirbean.framework.interactivity;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

public class ButtonEventListener implements EventListener {
    private final ConcurrentHashMap<Long, ExpiringKey> pages = new ConcurrentHashMap<>();

    // avoids the value having a reference to the key
    private final BlockingQueue<Long> expiringQueue = new PriorityBlockingQueue<>(10,
            (id1, id2) -> Long.compare(pages.get(id1).getLastInteraction(), pages.get(id2).getLastInteraction())
    );

    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.setName("Interactivity Cleanup Tread");
        return thread;
    });

    public ButtonEventListener() {
        service.scheduleWithFixedDelay(this::purge, 0, 20, TimeUnit.SECONDS);
    }

    private void purge() {
        Long id = expiringQueue.peek();
        while (id != null && pages.get(id).isExpired()) {
            expiringQueue.poll();
            pages.remove(id).end();

            id = expiringQueue.peek();
        }
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ButtonInteractionEvent buttonEvent) {
            ExpiringKey key = getKey(buttonEvent.getMessageIdLong());
            if (key == null) {
                return;
            }
            key.getInteractive().onEvent(buttonEvent);
            key.renew();
        } else if (event instanceof ShutdownEvent) {
            service.shutdown();
        }
    }

    private ExpiringKey getKey(long id) {
        return pages.get(id);
    }

    public void add(Long id, ExpiringKey key) {
        pages.put(id, key);
        expiringQueue.add(id);
    }
}