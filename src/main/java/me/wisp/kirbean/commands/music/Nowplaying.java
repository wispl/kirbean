package me.wisp.kirbean.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.utils.Text;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Nowplaying implements SlashCommand {
    @Command(name = "nowplaying", description = "Gets information of the current track")
    public void execute(SlashCommandInteractionEvent event) {
        GuildPlayer player = PlayerRepository.getOrCreatePlayer(event.getGuild());
        if (player.isIdle()) {
            event.reply("Nothing is playing...").setEphemeral(true).queue();
            return;
        }

        AudioTrack track = player.getCurrent();
        AudioTrackInfo info = track.getInfo();

        var position = track.getPosition();
        var duration = track.getDuration();
        var time = "[" + Text.prettyDuration(position) + "/" + Text.prettyDuration(duration) + "]\n";
        var progressbar = time + progressbar(position, duration);

        MessageEmbed embed = new EmbedBuilder().setTitle("Now Playing")
                .setThumbnail("https://img.youtube.com/vi/" + info.identifier + "/mqdefault.jpg")
                .addField("Title", Text.codeblock(info.title), false)
                .addField("Requester", track.getUserData(User.class).getAsTag(), true)
                .addField("Source", Text.link("Click", info.uri), true)
                .addField("Author", info.author, true)
                .addField("Progress", progressbar, false)
                .build();

        event.replyEmbeds(embed).queue();
    }

    private String progressbar(long position, long duration) {
        int barSize = 12;
        int fill = (int) ((barSize) * (position / duration));

        return "▇".repeat(fill) + "—".repeat(barSize - fill);
    }
}