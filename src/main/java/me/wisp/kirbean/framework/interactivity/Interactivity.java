package me.wisp.kirbean.interactivity;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;

import java.util.function.Consumer;

public class Interactivity {
    private static final ButtonEventListener listener = new ButtonEventListener();
    private static final Consumer<Message> deleteOnEnd = m -> m.delete().queue();
    private static final Consumer<Message> disableComponents = m -> m.editMessageComponents(
            m.getActionRows()
                    .stream()
                    .map(ActionRow::asDisabled).toList())
            .queue();

    public static void register(JDA jda) {
        jda.addEventListener(listener);
    }

    public static void createInteractive(SlashCommandInteractionEvent event, Interactive task) {
        reply(event, task);
    }

    private static void reply(SlashCommandInteractionEvent event, Interactive interactive) {
        event.deferReply().queue(
                hook -> hook.editOriginal(interactive.start()).queue(
                        m -> addTask(m, interactive)
                )
        );
    }

    public static void addTask(Message message, Interactive interactive, Runnable onEnd) {
        listener.add(message.getIdLong(), interactive);
        interactive.setOnEndAction(onEnd);
    }

    public static void addTask(Message message, Interactive interactive) {
        listener.add(message.getIdLong(), interactive);
        interactive.setOnEndAction(() -> disableComponents.accept(message));
    }

}
