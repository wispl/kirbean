package me.wisp.kirbean.commands.fun;

import me.wisp.kirbean.api.HTTPClient;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.net.URI;

public class Waifu implements SlashCommand {
    private static final URI WAIFU = URI.create("https://api.waifu.pics/sfw/waifu");
    @Command(name = "waifu", description = "Gets a waifu. Ever heard of outside?")
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle("2D > 3D?")
                .setImage(HTTPClient.get(WAIFU).get("url").asText())
                .setFooter(event.getMember().getEffectiveName() + ", you big weeb.\nImage from waifu.pics");
        event.replyEmbeds(eb.build()).queue();
    }
}
