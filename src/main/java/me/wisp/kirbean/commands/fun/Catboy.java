package me.wisp.kirbean.commands.fun;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.utils.Http;
import me.wisp.kirbean.utils.Json;
import me.wisp.kirbean.utils.Text;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Catboy implements SlashCommand {
    private final static String URL = "https://api.catboys.com/img";
    private final static ObjectReader READER = Json.readerFor(CatboyResponse.class);
    @Command(name = "catboy", description = "Fetches a catboy, another odd request...")
    public void execute(SlashCommandInteractionEvent event) {
        String response = Http.execute(event.getJDA(), URL);
        CatboyResponse res;
        try {
            res = READER.readValue(response);
        } catch (JsonProcessingException e) {
            event.reply("No catboys, too bad for you. What a wonderful day").queue();
            throw new RuntimeException(e);
        }
        var builder = new EmbedBuilder()
                .setTitle("This should probably be removed")
                .setImage(res.url)
                .setFooter(Text.link("Source", res.source_url));
        event.replyEmbeds(builder.build()).queue();
    }

    private static class CatboyResponse {
        public String url;
        public String source_url;
    }
}
