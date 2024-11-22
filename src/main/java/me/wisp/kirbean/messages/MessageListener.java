package me.wisp.kirbean.messages;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class MessageListener implements EventListener {
    @Override public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof MessageReceivedEvent messageEvent) {
            if (messageEvent.getMember().getUser().isBot()) {
                return;
            }
        }
    }

    private void handleMock(Message message, String content) {
       if (ThreadLocalRandom.current().nextInt(0, 100) < 1) {
           message.reply("```\n" + content + "\n```\nHahaha what an idiot").queue();
       }
    }
}