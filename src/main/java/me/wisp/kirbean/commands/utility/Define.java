package me.wisp.kirbean.commands.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.core.annotations.Option;
import me.wisp.kirbean.utils.Http;
import me.wisp.kirbean.utils.Json;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import okhttp3.HttpUrl;
import okhttp3.Request;

public class Define implements SlashCommand {
    private static final String URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    private static final ObjectReader reader = Json.readerFor(DefinitionResult.class);

    @Command(name = "define", description = "Looks up a word in the dictionary")
    @Option(name = "word", description = "word to look up")
    public void execute(SlashCommandInteractionEvent event) {
        var word = event.getOption("word").getAsString().toLowerCase();
        var url = HttpUrl.parse(URL).newBuilder()
                .addEncodedPathSegment(word).build();
        var request = new Request.Builder().url(url).build();
        String data = Http.execute(event.getJDA(), request);
        if (data == null) {
            event.reply("An error has occured...").setEphemeral(true).queue();
            return;
        }

        DefinitionResult result;
        try {
            result = reader.readValue(data);
        } catch (JsonProcessingException e) {
            event.reply("An error has occured...").setEphemeral(true).queue();
            return;
        }

        if (result.meanings.length == 0) {
            event.reply("No definitions found for word " + word).queue();
            return;
        }

        var builder = new EmbedBuilder()
                .setTitle(result.word)
                .addField("origin", result.origin, false)
                .addField("phonetics", result.phonetics, false);

        for (DefinitionResult.Meaning meaning : result.meanings) {
            builder.addField(meaning.partOfSpeech, parseDefinitions(meaning.definitions), false);
        }

        event.replyEmbeds(builder.build()).queue();
    }

    private String parseDefinitions(DefinitionResult.Definition definitions[]) {
        var builder = new StringBuilder();
        for (DefinitionResult.Definition definition : definitions) {
            builder.append("```\n•")
                    .append(definition.definition)
                    .append('\n')
                    .append("\t")
                    .append(definition.example)
                    .append("\n```");
        }
        return builder.toString();
    }

    private static class DefinitionResult {
        public String word;
        public String phonetics;
        public String origin;

        public Meaning[] meanings;

        private static class Meaning {
            public String partOfSpeech;
            public Definition[] definitions;

        }

        private static class Definition {
            public String definition;
            public String example;
            public String[] synonyms;
            public String[] antonyms;
        }
    }
}
