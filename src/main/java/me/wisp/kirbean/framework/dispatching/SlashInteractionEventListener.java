package me.wisp.kirbean.framework.dispatching;

import me.wisp.kirbean.framework.help.HelpMessageFactory;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class SlashInteractionEventListener implements EventListener {

    private final CommandDispatcher dispatcher;
    public SlashInteractionEventListener(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof SlashCommandInteractionEvent slashEvent) {
            if (!slashEvent.isFromGuild()) {
                slashEvent.reply("Get out of my DMs idiot...").queue();
            }
            dispatcher.dispatchCommand(slashEvent);
        }
    }
}
