package me.wisp.kirbean.interaction;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public abstract class Interactive {
    private long lastInteractionTime = System.currentTimeMillis();
    private Runnable onEndAction;

    public abstract MessageEmbed start();

    public abstract void handle(ButtonInteractionEvent event);

    public long getLastInteraction() {
        return lastInteractionTime;
    }
    public void renew() {
        lastInteractionTime = System.currentTimeMillis();
    }

    public void end() {
        onEndAction.run();
    }
    public void setOnEndAction(Runnable action) {
        onEndAction = action;
    }
}
