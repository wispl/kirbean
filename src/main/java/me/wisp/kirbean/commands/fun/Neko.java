package me.wisp.kirbean.commands.fun;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.utils.Http;
import me.wisp.kirbean.utils.Json;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Neko implements SlashCommand {
    private static final String URL = "https://nekos.life/api/v2/img/neko";

    @Command(name = "neko", description = "Gets a cat girl, odd... but okay")
    public void execute(SlashCommandInteractionEvent event) {
        String body = Http.execute(event.getJDA(), URL);
        try {
            String url = Json.readTree(body).get("url").asText();
            var builder = new EmbedBuilder()
                    .setTitle("I don't think this is a cat...")
                    .setImage(url)
                    .setFooter("Image from nekos.life");

            event.replyEmbeds(builder.build()).queue();
        } catch (JsonProcessingException e) {
            event.reply("Cannot find the darn cats").setEphemeral(true).queue();
            throw new RuntimeException(e);
        }
    }
}
