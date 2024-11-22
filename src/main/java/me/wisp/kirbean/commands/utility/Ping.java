package me.wisp.kirbean.commands.utility;

import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Ping implements SlashCommand {
    @Command( name = "ping", description = "Gets the rest and gateway ping!" )
    public void execute(SlashCommandInteractionEvent event) {
        JDA jda = event.getJDA();
        jda.getRestPing().queue(ping ->
            event.reply(String.format("Pong! Rest ping of %sms and gateway ping of %sms",
                    ping,
                    jda.getGatewayPing())).queue()
        );
    }
}

