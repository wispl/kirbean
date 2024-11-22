package me.wisp.kirbean.audio;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import me.wisp.kirbean.audio.player.GuildPlayer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerRepository {
    private static final DefaultAudioPlayerManager manager = new DefaultAudioPlayerManager();
    private static final Map<Long, GuildPlayer> players = new ConcurrentHashMap<>();

    public static void register(JDA jda) {
        /* TODO: This is currently implemented as a global map,
           look for a better implementation
         */
        jda.addEventListener(new VoiceEventListener());
        AudioSourceManagers.registerRemoteSources(manager);
        AudioSourceManagers.registerLocalSource(manager);
    }

    @Nullable
    public static GuildPlayer getPlayer(Guild guild) {
        return players.get(guild.getIdLong());
    }

    /**
     * Creates a player for the specified guild or returns the guild's player
     * if already exists. This will be used for most of the audio commands
     * because usage of an audio command usually signifies intent to use the
     * play command. If a player must exist then use {@link #getPlayer(Guild)}
     * @param guild guild to get the player of
     * @return the player for the guild
     */
    public static GuildPlayer getOrCreatePlayer(Guild guild) {
        return players.computeIfAbsent(guild.getIdLong(),
                k -> {
                    var player = new GuildPlayer(manager);
                    guild.getAudioManager().setSendingHandler(player);
                    return player;
                }
        );
    }
}
