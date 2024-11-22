package me.wisp.kirbean.audio.player;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.wisp.kirbean.utils.Text;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class LoadHandler implements AudioLoadResultHandler {
    private final SlashCommandInteractionEvent event;
    private final GuildPlayer player;
    private final String query;

    public LoadHandler(String query, SlashCommandInteractionEvent event, GuildPlayer player) {
        this.query = query;
        this.event = event;
        this.player = player;
    }
    private void load(AudioTrack track) {
        track.setUserData(event.getUser());
        player.enqueue(track);
    }

    @Override public void trackLoaded(AudioTrack track) {
        load(track);
        event.reply("Added "
                        + Text.bold(track.getInfo().title) + " to the queue!")
                .queue();
    }

    @Override public void playlistLoaded(AudioPlaylist playlist) {
        if (playlist.getTracks().size() == 0) {
            event.reply("Playlist is empty or could not be loaded").queue();
            return;
        }

        if (playlist.isSearchResult()) {
            AudioTrack track = playlist.getSelectedTrack() == null
                    ? playlist.getTracks().get(0)
                    : playlist.getSelectedTrack();

            load(track);
            event.reply("Added "
                            + Text.bold(track.getInfo().title) + "to the queue ")
                    .queue();
            return;
        }
        playlist.getTracks().forEach(this::load);
        event.reply("Added " + playlist.getTracks().size() + "from "
                        + Text.bold(playlist.getName()) + " to the queue.")
                .queue();
    }

    @Override public void noMatches() {
        event.reply("Nothing found for query: " + query)
                .setEphemeral(true)
                .queue();
    }

    @Override public void loadFailed(FriendlyException exception) {
        event.reply("Unable to load requested query due to error: " + exception)
                .setEphemeral(true)
                .queue();
    }
}