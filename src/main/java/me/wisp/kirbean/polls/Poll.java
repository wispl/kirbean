package me.wisp.kirbean.polls;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;

public interface Poll {
    Message start();
    void onEvent(GenericComponentInteractionCreateEvent event);
    Message end(Message message);
}
