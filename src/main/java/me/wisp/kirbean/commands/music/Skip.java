package me.wisp.kirbean.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Skip implements SlashCommand {
    @Command(name = "skip", description = "Skips the current track")
    public void execute(SlashCommandInteractionEvent event) {
        GuildPlayer player = PlayerRepository.getPlayer(event.getGuild());

        if (player.isIdle()) {
            event.reply("Nothing is playing...").setEphemeral(true).queue();
            return;
        }
        if (!inSameChannel(event.getMember(), player)) {
            event.reply("Join the voice channel to start a vote").setEphemeral(true).queue();
            return;
        }

        AudioTrack track = player.getCurrent();
        if (track.getUserData(User.class).getIdLong() == event.getMember().getIdLong()) {
            player.nextTrack();
            event.reply("Track requester skipped track").queue();
        }
    }

    private int getVoteGoal(int memberCount) {
       return memberCount/2 + 1;
    }

    private boolean inSameChannel(Member member, GuildPlayer player) {
        return member.getVoiceState().getChannel().getIdLong() == player.getVoiceChannel();
    }
}
