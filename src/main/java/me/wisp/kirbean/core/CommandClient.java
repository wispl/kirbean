package me.wisp.kirbean.core;

import me.wisp.kirbean.core.dispatching.CommandDispatcher;
import me.wisp.kirbean.core.dispatching.SlashInteractionEventListener;
import me.wisp.kirbean.core.help.HelpAutoCompleteListener;
import me.wisp.kirbean.core.repository.CommandRepository;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

public class CommandClient {
    private final CommandRepository repository;
    public CommandClient() {
        repository = new CommandRepository();
        repository.index();
    }

    public void start(JDA jda) {
        start(jda, true);
    }

    public void start(JDA jda, Guild guild) {
        new CommandUpdater(repository.asTree()).updateCommands(guild);
        start(jda, false);
    }

    public void cleanStart(JDA jda) {
        new CommandUpdater(repository.asTree()).purgeAndUpdateCommands(jda);
        start(jda, false);
    }

    public void start(JDA jda, boolean update) {
        if (update) {
            new CommandUpdater(repository.asTree()).updateCommands(jda);
        }
        jda.addEventListener(createListener());
        jda.addEventListener(new HelpAutoCompleteListener(repository.asTree().getCommands()));
    }

    private SlashInteractionEventListener createListener() {
        return new SlashInteractionEventListener(new CommandDispatcher(repository));
    }
}
