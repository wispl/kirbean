package me.wisp.kirbean.framework.interactivity;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface Interactive {
    Message start();
    void onEvent(ButtonInteractionEvent event);
}
