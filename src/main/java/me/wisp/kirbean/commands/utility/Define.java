package me.wisp.kirbean.commands.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import me.wisp.kirbean.api.HTTPClient;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import me.wisp.kirbean.framework.annotations.Option;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Define implements SlashCommand {
    private static final String DEFINE = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    @Command(name = "define", description = "Looks up a word in the dictionary")
    @Option(name = "word", description = "word to look up")
    public void execute(SlashCommandInteractionEvent event) {
        String word = event.getOption("word").getAsString().toLowerCase();
        JsonNode data = HTTPClient.getWithQuery(DEFINE, word);

        if (!data.has(0)) {
            event.reply("No definitions found for word " + word).queue();
            return;
        }
        data = data.get(0);

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(word)
                .addField("phonetics", parsePhonetics(data), false);

        for (JsonNode node : data.withArray("meanings")) {
            builder.addField(node.get("partOfSpeech").asText(), parseMeanings(node.withArray("definitions")), false);
        }

        event.replyEmbeds(builder.build()).queue();
    }

    private String parsePhonetics(JsonNode data) {
        return String.join(", ", data.get("phonetics").findValuesAsText("text"));
    }

    private String parseMeanings(ArrayNode definition) {
        StringBuilder definitions = new StringBuilder();
        for (JsonNode node : definition) {
            definitions.append("```\n•").append(node.get("definition").asText()).append('\n');
            if (node.has("example")) {
                   definitions.append('\t').append(node.get("example").asText());
            }
            definitions.append("\n```");
        }
        return definitions.toString();
    }
}
