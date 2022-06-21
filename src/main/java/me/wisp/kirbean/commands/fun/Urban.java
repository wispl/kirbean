package me.wisp.kirbean.commands.fun;

import com.fasterxml.jackson.databind.JsonNode;
import me.wisp.kirbean.api.HTTPClient;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import me.wisp.kirbean.framework.annotations.Option;
import me.wisp.kirbean.interaction.Interactivity;
import me.wisp.kirbean.interaction.pagination.Paginator;
import me.wisp.kirbean.interaction.pagination.impl.EmbedPages;
import me.wisp.kirbean.utils.Text;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;
import java.util.List;

public class Urban implements SlashCommand {
    private static final String URBAN = "https://api.urbandictionary.com/v0/define?term=";

    @Command(name = "urban", description = "Defines a word using urban dictionary, this is dangerous")
    @Option(name = "word", description = "word to get scared o- ermm, define")
    public void execute(SlashCommandInteractionEvent event) {
        String word = event.getOption("word").getAsString().toLowerCase();
        JsonNode data = HTTPClient.getWithQuery(URBAN, word);

        if (!data.has("list")) {
            event.reply("Could not find definition for " + word).queue();
            return;
        }

        EmbedBuilder builder = new EmbedBuilder();
        List<MessageEmbed> definitions = new ArrayList<>();

        data = data.withArray("list");
        for (int n = 0; n < data.size(); n++) {
            JsonNode node = data.get(n);
            definitions.add(builder
                    .setTitle(word)
                    .setDescription(node.get("definition").asText())
                    .clearFields()
                    .addField("Example", "```" + node.get("example").asText() + "```", false)
                    .addField("Author", node.get("author").asText(), true)
                    .addField("Written on", node.get("written_on").asText(), true)
                    .addField("Rating",
                            Text.rating(node.get("thumbs_up").asInt(), node.get("thumbs_down").asInt()),
                            true)
                    .setFooter(n + 1 + "/" + data.size())
                    .build());
        }

        EmbedPages pages = new EmbedPages(definitions);
        Interactivity.createPaginator(event, new Paginator(pages));
    }
}
