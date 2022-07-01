package me.wisp.kirbean.commands.music;

import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.audio.lyrics.LyricsClient;
import me.wisp.kirbean.audio.lyrics.LyricsData;
import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import me.wisp.kirbean.framework.interactivity.Interactivity;
import me.wisp.kirbean.interactive.paginator.Paginator;
import me.wisp.kirbean.interactive.paginator.impl.SimplePages;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.concurrent.ExecutionException;

public class Lyrics implements SlashCommand {

    private static final LyricsClient client = new LyricsClient();
    @Command(name = "lyrics", description = "Gets the lyric of the playing song")
    public void execute(SlashCommandInteractionEvent event) {
        GuildPlayer player = PlayerRepository.getPlayer(event.getGuild());
        if (player.isIdle()) {
            event.reply("The player is idle...").queue();
            return;
        }

        String title = player.getCurrent().getInfo().title;
        LyricsData data;
        try {
            data = client.getLyrics(title).get();
        } catch (ExecutionException | InterruptedException e) {
            event.reply("Could not find lyrics for " + title).queue();
            return;
        }

        SimplePages pages = new SimplePages(data.author(), data.title(), data.url(), data.lyrics());
        Interactivity.createInteractive(event, new Paginator(pages));
    }
}
