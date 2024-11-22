package me.wisp.kirbean.commands.fun;

import me.wisp.kirbean.api.HTTPClient;
import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Choices;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.interactive.EmbedSupplier;
import me.wisp.kirbean.utils.Text;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.net.URI;
import java.util.function.Supplier;

public class Animal implements SlashCommand {

    @Command(name = "animal", description = "Returns all forms of critters")
    @Choices(name="animal", description = "the animal to get",
            choices = {"bunny", "cat", "dog", "duck", "fox", "shibe", "bird"})
    public void execute(SlashCommandInteractionEvent event) {
        Animals animal = Animals.valueOf(event.getOption("animal").getAsString().toUpperCase());

        MessageEmbed template = new EmbedBuilder()
                .setTitle(animal.getTitle())
                .setFooter(Text.link("Source", animal.uri.toString()))
                .build();
        new EmbedSupplier(getSupplier(animal), template).start(event);
    }

    private Supplier<String> getSupplier(Animals endpoint) {
        var node = HTTPClient.get(endpoint.getUri());
        return switch (endpoint) {
            case SHIBE, BIRD -> () -> node.get(0).asText();
            case DOG, DUCK-> () -> node.get("url").asText();
            case FOX -> () -> node.get("image").asText();
            case BUNNY -> () -> node.get("media").get("gif").asText();
            case CAT -> () -> node.get(0).get("url").asText();
        };
    }

    enum Animals {
        SHIBE("Shibe", "https://shibe.online/api/shibes"),
        BIRD("My name is Yoshikage Kira. I'm 33 years old. My house is in the northeast section of Morioh",
                "https://shibe.online/api/birds"),
        DUCK("This is quackers", "https://random-d.uk/api/v2/quack"),
        DOG("Woof!", "https://random.dog/woof.json"),
        CAT("Mew?","https://api.thecatapi.com/v1/images/search"),
        BUNNY("Carrot", "https://api.bunnies.io/v2/loop/random/?media=gif,png"),
        FOX("Yip yip?", "https://randomfox.ca/floof/");

        private final String title;
        private final URI uri;

        Animals(String title, String uri) {
            this.title = title;
            this.uri = URI.create(uri);
        }

        public URI getUri() {
            return uri;
        }

        public String getTitle() {
            return title;
        }
    }
}

