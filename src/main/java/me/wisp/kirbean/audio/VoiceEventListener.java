package me.wisp.kirbean.audio;

import me.wisp.kirbean.audio.player.GuildPlayer;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class VoiceEventListener implements EventListener {

    @Override public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildVoiceLeaveEvent) {
            onLeave((GuildVoiceLeaveEvent) event);
        } else if (event instanceof GuildVoiceJoinEvent) {
            onJoin((GuildVoiceJoinEvent) event);
        }
    }

    public void onJoin(GuildVoiceJoinEvent event) {
        if (!event.getMember().getUser().isBot()) {
            PlayerRepository.getPlayer(event.getGuild()).resume();
        }
    }

    public void onLeave(GuildVoiceLeaveEvent event) {
        GuildPlayer player = PlayerRepository.getPlayer(event.getGuild());
        if (event.getMember().getIdLong() == event.getGuild().getSelfMember().getIdLong()) {
           player.reset();
           return;
        }

        if (player.membersInVoiceChannel() == 0) {
            player.pause();
            if (player.isIdle()) player.leave();
        }
    }
}
