package me.wisp.kirbean.commands.fun;

import me.wisp.kirbean.api.HTTPClient;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.net.URI;

public class Neko implements SlashCommand {
    private static final URI NEKO = URI.create("https://nekos.life/api/v2/img/neko");
    @Command(name = "neko", description = "Gets a cat girl, odd... but okay")
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle("Nyah?!?!?!")
                .setImage(HTTPClient.get(NEKO).get("url").asText())
                .setFooter("This isn't a cat?!?!?\nImage from nekos.life");

        event.replyEmbeds(eb.build()).queue();
    }
}
