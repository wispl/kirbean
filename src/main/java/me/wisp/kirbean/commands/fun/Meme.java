package me.wisp.kirbean.commands.fun;

import com.fasterxml.jackson.databind.JsonNode;
import me.wisp.kirbean.api.GuildCache;
import me.wisp.kirbean.api.HTTPClient;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import me.wisp.kirbean.utils.Text;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.net.URI;
import java.util.ArrayDeque;

public class Meme implements SlashCommand {

    private final GuildCache cache = new GuildCache(this::populate);
    private static final URI MEME = URI.create("https://www.reddit.com/r/memes/top/.json?t=day&limit=100");
    @Command(name = "meme", description = "Gets a meme, nice")
    public void execute(SlashCommandInteractionEvent event) {
        JsonNode response = cache.get(event.getGuild().getIdLong());
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle(response.get("title").asText(), "https://www.reddit.com/" + response.get("permalink").asText())
                .setImage(response.get("url").asText())
                .setFooter(Text.rating(response.get("ups").asInt(), response.get("downs").asInt())
                        + " 🗩 "
                        + response.get("num_comments").asText()
                );

        event.replyEmbeds(eb.build()).queue();
    }

    private ArrayDeque<JsonNode> populate() {
        ArrayDeque<JsonNode> responses = new ArrayDeque<>();
        for (JsonNode node : HTTPClient.get(MEME).get("data").withArray("children")) {
            JsonNode data = node.get("data");
            if (data.get("post_hint").asText().equals("image")) {
                responses.add(data);
            }
        }
        return responses;
    }
}
