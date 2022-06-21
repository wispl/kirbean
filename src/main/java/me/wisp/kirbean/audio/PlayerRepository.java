package me.wisp.kirbean.audio;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import me.wisp.kirbean.audio.player.GuildPlayer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerRepository {
    private static final DefaultAudioPlayerManager manager = new DefaultAudioPlayerManager();
    private static final Map<Long, GuildPlayer> players = new ConcurrentHashMap<>();

    public static void register(JDA jda) {
        jda.addEventListener(new VoiceEventListener());
        AudioSourceManagers.registerRemoteSources(manager);
        AudioSourceManagers.registerLocalSource(manager);
    }

    public static GuildPlayer getPlayer(Guild guild) {
        return players.computeIfAbsent(guild.getIdLong(), guildId -> new GuildPlayer(guild, manager, manager.createPlayer()));
    }
}
