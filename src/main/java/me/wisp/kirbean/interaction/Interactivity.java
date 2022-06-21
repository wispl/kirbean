package me.wisp.kirbean.interaction;

import me.wisp.kirbean.interaction.pagination.Paginator;
import me.wisp.kirbean.interaction.supplier.EmbedSupplier;
import me.wisp.kirbean.interaction.voting.Vote;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;

import java.util.function.Consumer;

public class Interactivity {
    private static final ButtonEventListener listener = new ButtonEventListener();
    private static final Consumer<Message> deleteOnEnd = m -> m.delete().queue();
    private static final Consumer<Message> removeComponents = m -> m.editMessageComponents(
            m.getActionRows()
                    .stream()
                    .map(ActionRow::asDisabled).toList())
            .queue();

    public static void register(JDA jda) {
        jda.addEventListener(listener);
    }

    public static void createPaginator(SlashCommandInteractionEvent event, Paginator task) {
        reply(event, task, Buttons.createDefaultPagination());
    }

    public static void createSupplier(SlashCommandInteractionEvent event, EmbedSupplier task) {
        reply(event, task, Buttons.createDefaultSupplier());
    }

    public static void createVote(SlashCommandInteractionEvent event, Vote task) {
        reply(event, task, Buttons.createDefaultVote());
    }

    private static void reply(SlashCommandInteractionEvent event, Interactive interactive, ActionRow rows) {
        event.deferReply()
                .queue(hook -> hook.editOriginalEmbeds(interactive.start()).setActionRows(rows)
                        .queue(m -> addTask(m, interactive, () -> removeComponents.accept(m))));
    }

    public static void addTask(Message message, Interactive interactive, Runnable onEnd) {
        listener.add(message.getIdLong(), interactive);
        interactive.setOnEndAction(onEnd);
    }

    public static void addTask(Message message, Interactive interactive) {
        listener.add(message.getIdLong(), interactive);
        interactive.setOnEndAction(() -> removeComponents.accept(message));

    }

}
