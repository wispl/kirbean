package me.wisp.kirbean.commands.fun;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.utils.Http;
import me.wisp.kirbean.utils.Json;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Waifu implements SlashCommand {
    private static final String URL = "https://api.waifu.pics/sfw/waifu";
    @Command(name = "waifu", description = "Gets a waifu. Ever heard of outside?")
    public void execute(SlashCommandInteractionEvent event) {
        String response = Http.execute(event.getJDA(), URL);
        String image;
        try {
            image = Json.readTree(response).get("url").asText();
        } catch (JsonProcessingException e) {
            event.reply("Uh oh, something is wrong").queue();
            throw new RuntimeException();
        }

        var builder = new EmbedBuilder()
                .setTitle("2D > 3D?")
                .setImage(image)
                .setFooter("Image from waifu.pics");
        event.replyEmbeds(builder.build()).queue();
    }
}
