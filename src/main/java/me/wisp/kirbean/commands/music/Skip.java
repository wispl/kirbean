package me.wisp.kirbean.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.audio.tracks.UserInfo;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import me.wisp.kirbean.framework.interactivity.Interactivity;
import me.wisp.kirbean.interactive.vote.Vote;
import me.wisp.kirbean.interactive.vote.VotePage;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Skip implements SlashCommand {
    @Command(name = "skip", description = "Skips the current track")
    public void execute(SlashCommandInteractionEvent event) {
        GuildPlayer player = PlayerRepository.getPlayer(event.getGuild());
        AudioTrack track = player.getCurrent();

        if (!checkSameChannel(event.getMember(), player)) {
            event.reply("You must be in my voice channel to initiate a skip").setEphemeral(true).queue();
            return;
        }

        if (player.isIdle()) {
            event.reply("Nothing is playing right, just like how there is nothing in your head").setEphemeral(true).queue();
            return;
        }

        if (((UserInfo) track.getUserData()).getRequesterId() == event.getMember().getIdLong()) {
            event.reply("Skipped track since it was requested by requester").queue();
            player.nextTrack();
            return;
        }

        VotePage page = new VotePage("Skip track votes",
                event.getMember().getEffectiveName() + " wants to skip " + track.getInfo().title,
                getVoteGoal(player.membersInVoiceChannel()));

        Vote vote = new Vote(m -> checkSameChannel(m, player), page, () -> {
            if (player.getCurrent().equals(track)) {
                player.nextTrack();
                event.getHook().sendMessage("Track skipped").queue();
                return;
            }

            event.getHook().sendMessage("The track has already ended").queue();
        });

        Interactivity.createInteractive(event, vote);
    }

    private int getVoteGoal(int memberCount) {
       return memberCount/2 + 1;
    }

    private boolean checkSameChannel(Member member, GuildPlayer player) {
        return member.getVoiceState().getChannel().getIdLong() == player.getVoiceChannel();
    }
}
