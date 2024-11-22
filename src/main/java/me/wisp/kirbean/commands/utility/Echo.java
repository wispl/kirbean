package me.wisp.kirbean.commands.utility;

import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.core.annotations.Option;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Echo implements SlashCommand {
    @Command(name = "echo", description = "Say a phrase")
    @Option(name = "phrase", description = "the phrase to echo")
    public void execute(SlashCommandInteractionEvent event) {
        event.reply(event.getOption("phrase").getAsString()).queue();
    }
}
