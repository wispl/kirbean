package me.wisp.kirbean.framework.help;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HelpAutoCompleteListener implements EventListener {

    private final List<String> options;

    public HelpAutoCompleteListener(List<String> options) {
        this.options = options;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof CommandAutoCompleteInteractionEvent auto) {
            if (auto.getName().equals("help")) {
                auto.replyChoices(options.stream().filter(o -> o.startsWith(auto.getFocusedOption().getValue()))
                        .map(o -> new Command.Choice(o, o))
                        .toList()
                ).queue();
            }
        }
    }
}
