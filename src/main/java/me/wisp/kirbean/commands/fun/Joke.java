package me.wisp.kirbean.commands.fun;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.utils.Text;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class Joke implements SlashCommand {
    private static final String URL = "https://v2.jokeapi.dev/joke/Any?blacklistFlags=nsfw,racist,sexist,explicit";
    private static final ObjectReader READER = new ObjectMapper().readerFor(Result.class);

    @Command(name = "joke", description = "Fetches a joke, but don't worry, you will always be the biggest joke here.")
    public void execute(SlashCommandInteractionEvent event) {
        Request request = new Request.Builder().url(URL).build();
        Result joke;
        try (Response response = event.getJDA().getHttpClient().newCall(request).execute()) {
            joke = READER.readValue(response.body().toString());
        } catch (IOException e) {
            event.reply("Could not fetch jokes. This is a sad day for dads :(").queue();
            return;
        }

        var builder = new EmbedBuilder().setTitle(joke.category + " Jokes")
                .setFooter("Did you get it? " + "Jokes from jokeapi.dev");
        if (joke.type.equals("single")) {
            builder.setDescription(joke.joke);
        } else {
            builder.setDescription(joke.setup + "/n" + Text.spoiler(joke.delivery));
        }
        event.replyEmbeds(builder.build()).queue();
    }

    private static class Result {
        public String category;
        public String type;
        public String joke;

        public String setup;
        public String delivery;
    }
}
