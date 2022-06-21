package me.wisp.kirbean.commands.fun;

import com.fasterxml.jackson.databind.JsonNode;
import me.wisp.kirbean.api.HTTPClient;
import me.wisp.kirbean.interaction.Interactivity;
import me.wisp.kirbean.interaction.supplier.SupplierPage;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import me.wisp.kirbean.interaction.supplier.EmbedSupplier;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.net.URI;

public class Joke implements SlashCommand {
    private static final URI JOKE = URI.create("https://v2.jokeapi.dev/joke/Any?blacklistFlags=nsfw,racist,sexist,explicit");

    @Command(name = "joke", description = "Fetches a joke, but don't worry, you will always be the biggest joke here.")
    public void execute(SlashCommandInteractionEvent event) {
        SupplierPage page = new SupplierPage("Joke", this::getJoke, "Did you get it?", false);
        Interactivity.createSupplier(event, new EmbedSupplier(page));
    }

    private String getJoke() {
        JsonNode data = HTTPClient.get(JOKE);
        String type = "```\n" + data.get("category").asText() + "\n```\n";
        if (data.get("type").asText().equals("single")) {
            return type + data.get("joke").asText();
        }

        return type + data.get("setup").asText() + "\n||" + data.get("delivery").asText() + "||";
    }
}
