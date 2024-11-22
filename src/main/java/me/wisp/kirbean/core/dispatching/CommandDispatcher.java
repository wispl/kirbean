package me.wisp.kirbean.core.dispatching;

import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.help.HelpMessageFactory;
import me.wisp.kirbean.core.repository.CommandRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class CommandDispatcher {
    private final Logger logger = LoggerFactory.getLogger(CommandDispatcher.class);
    private final Map<String, SlashCommand> commands;
    private final HelpMessageFactory help;
    private final ScheduledExecutorService pool = Executors.newScheduledThreadPool(
            Runtime.getRuntime().availableProcessors() + 1,
             r -> {
                Thread thread = new Thread(r);
                thread.setDaemon(false);
                thread.setUncaughtExceptionHandler((t, e) -> logger.warn("Exception in thread" + t.getName() + "': ", e));
                thread.setName("Command Pool " + thread.getName());
                return thread;
             });

    public CommandDispatcher(CommandRepository repository) {
        this.commands = repository.asMap();
        this.help = new HelpMessageFactory(repository.asTree());
    }

    public void dispatchCommand(SlashCommandInteractionEvent event) {
        String id = event.getFullCommandName();
        if (id.equals("help")) {
            String option = event.getOption("command", null, OptionMapping::getAsString);
            event.replyEmbeds(help.getHelpForCommand(option)).queue();
            return;
        }

        SlashCommand command = commands.get(id);
        pool.submit(() -> {
            try {
                command.execute(event);
            } catch (Throwable e) {
                logger.warn("Error message: " + e.getMessage(), e);
            }
        });
    }
}
