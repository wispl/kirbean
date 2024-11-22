package me.wisp.kirbean.commands.music;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.interactive.Paginator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.SplitUtil;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lyrics implements SlashCommand {

    private static final ObjectReader reader = new ObjectMapper().reader();
    private static final String GENIUS = "https://genius.com/api/search?q=";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; rv:91.0) Gecko/20100101 Firefox/91.0";
    @Command(name = "lyrics", description = "Gets the lyric of the playing song")
    public void execute(SlashCommandInteractionEvent event) {
        GuildPlayer player = PlayerRepository.getPlayer(event.getGuild());
        if (player.isIdle()) {
            event.reply("The player is idle...").queue();
            return;
        }

        Request request = new Request.Builder()
                .url(GENIUS)
                .build();
        try (Response r = event.getJDA().getHttpClient().newCall(request).execute()) {
            // get url of the lyrics page
            String url = reader.readTree(r.body().toString())
                    .at("/response/hits/0/result/url")
                    .asText();

            Document document = Jsoup.connect(url).userAgent(USER_AGENT).get();

            List<MessageEmbed> embeds = new ArrayList<>();
            List<String> lyrics = SplitUtil.split(
                    getLyrics(document),
                    1024,
                    SplitUtil.Strategy.NEWLINE);

            var builder = new EmbedBuilder()
                    .setTitle(getTitle(document), url)
                    .setAuthor(getAuthor(document));

            int count = 1;
            for (final var chunk : lyrics) {
                embeds.add(builder.setDescription(chunk)
                        .setFooter("Page " + count + "/" + lyrics.size())
                        .build());
                count++;
            }

            new Paginator(embeds).start(event);
        } catch (IOException ex) {
            event.reply("Could not find lyrics").queue();
        }
    }

    // removes parenthesis and everything inside them, seems to get better results
    private String sanitizeQuery(String search) {
        return search.replaceAll("\\(.*\\)", "");
    }

    private String getTitle(Document document) {
        return document
                .selectFirst("span[class^=SongHeaderVariantdesktop__HiddenMask]")
                .ownText();
    }

    private String getAuthor(Document document) {
        return document
                .selectFirst("a[class*=SongHeaderVariantdesktop__Artist]")
                .ownText();
    }

    private String getLyrics(Document document) {
        return document
                .selectFirst("div[class^=Lyrics__Container]")
                .ownText();
    }
}
