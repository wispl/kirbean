package me.wisp.kirbean.commands.fun;

import me.wisp.kirbean.api.HTTPClient;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.net.URI;

public class Catboy implements SlashCommand {
    private final static URI CATBOY = URI.create("https://api.catboys.com/img");
    @Command(name = "catboy", description = "Fetches a catboy, another odd request...")
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle("Ps ps ps ps ps? What...")
                .setImage(HTTPClient.get(CATBOY).get("url").asText())
                .setFooter("Please don't use the neko command as well...");
        event.replyEmbeds(eb.build()).queue();

    }
}
