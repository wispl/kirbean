package me.wisp.kirbean.commands.fun;

import me.wisp.kirbean.api.HTTPClient;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Choices;
import me.wisp.kirbean.framework.annotations.Command;
import me.wisp.kirbean.framework.interactivity.Interactivity;
import me.wisp.kirbean.interactive.supplier.EmbedSupplier;
import me.wisp.kirbean.interactive.supplier.SupplierPage;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.net.URI;
import java.util.function.Supplier;

public class Animal implements SlashCommand {

    @Command(name = "animal", description = "Returns all forms of critters")
    @Choices(name="animal", description = "the animal to get",
            choices = {"bunny", "cat", "dog", "duck", "fox", "shibe", "bird"})
    public void execute(SlashCommandInteractionEvent event) {
        Animals animal = Animals.valueOf(event.getOption("animal").getAsString().toUpperCase());

        SupplierPage page = new SupplierPage(animal.title, getSupplier(animal), animal.footer);
        Interactivity.createInteractive(event, new EmbedSupplier(page));
    }

    private Supplier<String> getSupplier(Animals endpoint) {
        return switch (endpoint) {
            case SHIBE, BIRD -> () -> HTTPClient.get(endpoint.uri).get(0).asText();
            case DOG, DUCK-> () -> HTTPClient.get(endpoint.uri).get("url").asText();
            case FOX -> () -> HTTPClient.get(endpoint.uri).get("image").asText();
            case BUNNY -> () -> HTTPClient.get(endpoint.uri).get("media").get("gif").asText();
            case CAT -> () -> HTTPClient.get(endpoint.uri).get(0).get("url").asText();
        };
    }

    private enum Animals {
        SHIBE("Shiba cute", "https://shibe.online/api/shibes","Image from shibe.online"),
        BIRD("My name is Yoshikage Kira. I'm 33 years old. My house is in the northeast section of Morioh, where all the villas are, and I am not married. I....", "https://shibe.online/api/birds", "Image from shibe.online"),
        DUCK("This is quackers", "https://random-d.uk/api/v2/quack","Image from random-d.uk"),
        DOG("Woof!", "https://random.dog/woof.json","Image from random.dog"),
        CAT("Mew?","https://api.thecatapi.com/v1/images/search","Image from random.cat"),
        BUNNY("Carrot", "https://api.bunnies.io/v2/loop/random/?media=gif,png", "Image from bunnies.io"),
        FOX("Yip yip?", "https://randomfox.ca/floof/", "Image from randomfox.ca");

        private final String title;
        private final URI uri;
        private final String footer;
        Animals(String title, String uri, String footer) {
            this.title = title;
            this.uri = URI.create(uri);
            this.footer = footer;
        }
    }
}