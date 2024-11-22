package me.wisp.kirbean.commands.music;

import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.audio.player.LoadHandler;
import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.core.annotations.Option;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.regex.Pattern;

public class Play implements SlashCommand {

    private static final Pattern URL = Pattern.compile(
            "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+"
    );

    @Command(name = "play", description = "Join a channel and play some music")
    @Option(name = "query", description = "link or search term")
    public void execute(SlashCommandInteractionEvent event) {
        var query = event.getOption("query").getAsString();
        // ensure user is in a guild
        var guild = event.getGuild();

        var channel = event.getMember().getVoiceState().getChannel();
        if (channel == null) {
            event.reply("You are not in a voice channel!")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        GuildPlayer player = PlayerRepository.getOrCreatePlayer(guild);
        if (!guild.getSelfMember().getVoiceState().inAudioChannel()) {
            guild.getAudioManager().openAudioConnection(channel);
            player.setBroadcastChannel(event.getMessageChannel());
        } else if (channel != guild.getSelfMember().getVoiceState().getChannel()) {
            event.reply("You are not in the same voice channel as me")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        String search = URL.matcher(query).matches() ? query : "ytsearch: " + query;
        player.play(search, new LoadHandler(query, event, player));
    }
}