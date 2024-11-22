package me.wisp.kirbean.audio;

import me.wisp.kirbean.audio.player.GuildPlayer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class VoiceEventListener implements EventListener {

    @Override public void onEvent(@NotNull GenericEvent e) {
        // TODO: Do we even need this? We can probably simplify this quite a bit
        if (e instanceof GuildVoiceUpdateEvent event) {
            Guild guild = event.getGuild();
            GuildPlayer player = PlayerRepository.getPlayer(guild);
            if (player == null) {
                return;
            }

            if (event.getChannelJoined() == null) {
                // the member who left was the bot
                if (event.getMember() == guild.getSelfMember()) {
                    player.reset();
                }

                long members = event.getChannelLeft()
                        .getMembers()
                        .stream()
                        .filter(member -> !member.getUser().isBot())
                        .count();
                if (members == 0) {
                    guild.getAudioManager().closeAudioConnection();
                }
            } else {
                // resume playing if a member joined the channel
                if (!event.getMember().getUser().isBot()) {
                    player.resume();
                }
            }
        }
    }
}
