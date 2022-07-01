package me.wisp.kirbean.polls;

import me.wisp.kirbean.polls.impl.SimplePoll;
import me.wisp.kirbean.polls.page.PollPage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Polls {
    private static final PollListener listener = new PollListener();
    private final static ScheduledExecutorService timeout = Executors.newSingleThreadScheduledExecutor();

    public static void register(JDA jda) {
        jda.addEventListener(listener);
    }

    public static void createPoll(TextChannel channel, PollPage page, long duration, TimeUnit unit) {
        SimplePoll poll = new SimplePoll(page, duration, unit);
        channel.sendMessage(poll.start()).queue(m -> addPoll(m, poll, duration, unit));
    }

    public static void createPoll(SlashCommandInteractionEvent event, PollPage page, long duration, TimeUnit unit) {
        SimplePoll poll = new SimplePoll(page, duration, unit);
        event.reply(poll.start())
                .queue(h -> h.retrieveOriginal()
                        .queue(m -> addPoll(m, poll, duration, unit)
                        )
                );
    }

    private static void addPoll(Message message, Poll poll, long duration, TimeUnit unit) {
        listener.add(message.getIdLong(), poll);
        timeout.schedule(() -> {
            listener.remove(message.getIdLong());
            message.editMessageComponents().queue();
            message.getChannel().sendMessage(poll.end(message)).queue();
        }, duration, unit);
    }
}
