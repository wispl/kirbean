package me.wisp.kirbean.polls;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class PollListener implements EventListener {
    private final static HashMap<Long, Poll> polls = new HashMap<>();

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GenericComponentInteractionCreateEvent componentEvent) {
            Poll poll = polls.get(componentEvent.getMessage().getIdLong());
            if (poll == null) {
                return;
            }

            poll.onEvent(componentEvent);
        }
    }

    public void add(Long id, Poll poll) {
        polls.put(id, poll);
    }

    public void remove(Long id) {
        polls.remove(id);
    }
}
