package me.wisp.kirbean.commands.fun;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.core.annotations.Option;
import me.wisp.kirbean.interactive.Paginator;
import me.wisp.kirbean.utils.Http;
import me.wisp.kirbean.utils.Json;
import me.wisp.kirbean.utils.Text;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import okhttp3.*;

import java.util.ArrayList;
import java.util.List;

public class Urban implements SlashCommand {
    // private static final String URL = "https://api.urbandictionary.com/v0/define?term=";
    private static final String URL = "https://api.urbandictionary.com/v0/define";
    private static final ObjectReader reader = Json.readerForListOf(UrbanResponse.class);

    @Command(name = "urban", description = "Defines a word using urban dictionary, this is dangerous")
    @Option(name = "word", description = "word to get scared o- ermm, define")
    public void execute(SlashCommandInteractionEvent event) {
        var word = event.getOption("word").getAsString().toLowerCase();

        var url = HttpUrl.parse(URL).newBuilder()
                .addQueryParameter("term", word)
                .build();
        String response = Http.execute(event.getJDA(), url);
        try {
            List<UrbanResponse> results = reader.readValue(response);
            if (results.size() == 0) {
                event.reply("No definitions found").setEphemeral(true).queue();
                return;
            }
            new Paginator(parseDefinitions(word, results)).start(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<MessageEmbed> parseDefinitions(String word, List<UrbanResponse> results) {
        var definitions = new ArrayList<MessageEmbed>();
        var builder = new EmbedBuilder();

        var count = 1;
        for (final var result : results) {
            definitions.add(builder.setTitle(word, result.permalink)
                    .setDescription(result.definition)
                    .clearFields()
                    .addField("Author", result.author, false)
                    .addField("Example", Text.codeblock(result.example), false)
                    .addField("Written on", result.written_on.substring(0, 10),true)
                    .addField("Rating", result.thumbs_up + "↑, "
                            + result.thumbs_down + "↓", true)
                    .setFooter(count + "/" + definitions.size())
                    .build());
        }
        return definitions;
    }

    private static class UrbanResponse {
        public String definition;
        public String example;
        public String author;
        public String written_on;
        public String permalink;

        public int thumbs_up;
        public int thumbs_down;
    }
}