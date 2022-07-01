package me.wisp.kirbean.framework.collector;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.Modal;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Collector {
    private static final ModalInteractionListener listener = new ModalInteractionListener();

    public static void register(JDA jda) {
        jda.addEventListener(listener);
    }

    public static CompletableFuture<List<String>> createModal(SlashCommandInteractionEvent event, Modal modal) {
        event.replyModal(modal).queue();
        return listener.add(event.getMember().getIdLong());
    }
}
