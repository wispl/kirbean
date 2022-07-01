package me.wisp.kirbean.commands.fun;

import com.fasterxml.jackson.databind.JsonNode;
import me.wisp.kirbean.api.HTTPClient;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import me.wisp.kirbean.framework.annotations.Option;
import me.wisp.kirbean.framework.interactivity.Interactivity;
import me.wisp.kirbean.interactive.paginator.Paginator;
import me.wisp.kirbean.interactive.paginator.impl.EmbedPages;
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
        JsonNode data = HTTPClient.getWithQuery(URBAN, word).withArray("list");

        if (data.size() == 0) {
            event.reply("No definitions found for word " + word).queue();
            return;
        }

        EmbedBuilder builder = new EmbedBuilder();
        List<MessageEmbed> definitions = new ArrayList<>();

        for (int n = 0; n < data.size(); n++) {
            JsonNode node = data.get(n);
            definitions.add(builder
                    .setTitle(word)
                    .setDescription(node.get("definition").asText())
                    .clearFields()
                    .addField("Example", "```\n" + node.get("example").asText() + "\n```", false)
                    .addField("Author", node.get("author").asText(), true)
                    .addField("Written on", node.get("written_on").asText().substring(0, 10), true)
                    .addField("Rating",
                            Text.rating(node.get("thumbs_up").asInt(), node.get("thumbs_down").asInt()),
                            true)
                    .setFooter(n + 1 + "/" + data.size())
                    .build());
        }

        Interactivity.createInteractive(event, new Paginator(new EmbedPages(definitions)));
    }
}
