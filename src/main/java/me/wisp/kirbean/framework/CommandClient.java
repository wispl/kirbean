package me.wisp.kirbean.framework;

import me.wisp.kirbean.framework.dispatching.CommandDispatcher;
import me.wisp.kirbean.framework.dispatching.SlashInteractionEventListener;
import me.wisp.kirbean.framework.help.HelpAutoCompleteListener;
import me.wisp.kirbean.framework.repository.CommandRepository;
import net.dv8tion.jda.api.JDA;

public class CommandClient {
    private final CommandRepository repository;
    public CommandClient() {
        repository = new CommandRepository();
        repository.index();
    }

    public void start(JDA jda) {
        jda.addEventListener(createListener());
        jda.addEventListener(new HelpAutoCompleteListener(repository.asTree().getCommands()));
    }

    public CommandUpdater createUpdater() {
        return new CommandUpdater(repository.asTree());
    }

    private SlashInteractionEventListener createListener() {
        return new SlashInteractionEventListener(new CommandDispatcher(repository));
    }
}
