package me.wisp.kirbean.framework.collector;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModalInteractionListener implements EventListener {
    private final HashMap<Long, CompletableFuture<List<String>>> collectors = new HashMap<>();

    @Override public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ModalInteractionEvent modalEvent) {
           CompletableFuture<List<String>> future = collectors.get(modalEvent.getMember().getIdLong());
           future.complete(modalEvent.getValues().stream().map(ModalMapping::getAsString).toList());
           collectors.remove(modalEvent.getMember().getIdLong());
           ((ModalInteractionEvent) event).reply("Reply received!").setEphemeral(true).queue();
        }
    }

    public CompletableFuture<List<String>> add(long id) {
        CompletableFuture<List<String>> completableFuture = new CompletableFuture<>();
        collectors.put(id, completableFuture);
        return completableFuture;
    }
}
