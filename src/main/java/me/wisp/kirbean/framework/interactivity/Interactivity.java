package me.wisp.kirbean.framework.interactivity;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;

import java.util.function.Consumer;

public class Interactivity {
    private static final ButtonEventListener listener = new ButtonEventListener();
    private static final Consumer<Message> disableComponents = m -> m.editMessageComponents(
            m.getActionRows()
                    .stream()
                    .map(ActionRow::asDisabled).toList())
            .queue();

    public static void register(JDA jda) {
        jda.addEventListener(listener);
    }


    public static void createInteractive(SlashCommandInteractionEvent event, Interactive interactive) {
        createInteractive(event, interactive, 60000);
    }

    public static void createInteractive(SlashCommandInteractionEvent event, Interactive interactive, long timeout) {
        reply(event, interactive, timeout);
    }

    public static void createInteractive(SlashCommandInteractionEvent event, Interactive interactive, long timeout, Runnable onEnd) {
        reply(event, interactive, timeout, onEnd);
    }

    public static void createInteractive(TextChannel channel, Interactive task) {
        channel.sendMessage(task.start()).queue(m -> addTask(m, task, 60000, () -> disableComponents.accept(m)));
    }

    private static void reply(SlashCommandInteractionEvent event, Interactive interactive, long timeout, Runnable onEnd) {
        event.reply(interactive.start()).queue(h -> h.retrieveOriginal()
                        .queue(m -> addTask(m, interactive, timeout, () -> {
                            onEnd.run();
                            disableComponents.accept(m);
                        }))
        );
    }

    private static void reply(SlashCommandInteractionEvent event, Interactive interactive, long timeout) {
        event.deferReply().queue(
                hook -> hook.editOriginal(interactive.start()).queue(
                        m -> addTask(m, interactive, timeout, () -> disableComponents.accept(m))
                )
        );
    }

    private static void addTask(Message message, Interactive interactive, long timeout, Runnable onEnd) {
        listener.add(message.getIdLong(), new ExpiringKey(interactive, timeout, onEnd));
    }
}
