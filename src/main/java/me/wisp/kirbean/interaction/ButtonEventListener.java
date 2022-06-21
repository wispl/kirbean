package me.wisp.kirbean.interaction;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ButtonEventListener implements EventListener {
    private static final Logger logger = LoggerFactory.getLogger(ButtonEventListener.class);
    private final ConcurrentHashMap<Long, Interactive> pages = new ConcurrentHashMap<>();

    // avoids the value having a reference to the key
    private final BlockingQueue<Long> expiringQueue = new PriorityBlockingQueue<>(10,
            (id1, id2) -> Long.compare(pages.get(id1).getLastInteraction(), pages.get(id2).getLastInteraction())
    );

    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    public ButtonEventListener() {
        service.scheduleWithFixedDelay(this::purge, 0, 5, TimeUnit.SECONDS);
    }

    private void purge() {
        Long messageId = expiringQueue.peek();
        while (messageId != null && System.currentTimeMillis() - pages.get(messageId).getLastInteraction() >= 60000) {
            expiringQueue.poll();
            pages.remove(messageId).end();

            messageId = expiringQueue.peek();
        }
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ButtonInteractionEvent buttonEvent) {
            process(buttonEvent);
        } else if (event instanceof ShutdownEvent) {
            service.shutdown();
        }
    }

    private void process(ButtonInteractionEvent event) {
        Interactive interactive = pages.get(event.getMessageIdLong());
        if (interactive != null) {
            try {
                interactive.renew();
                interactive.handle(event);
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
    }

    public void add(Long id, Interactive interactive) {
        pages.put(id, interactive);
        expiringQueue.add(id);
    }
}
