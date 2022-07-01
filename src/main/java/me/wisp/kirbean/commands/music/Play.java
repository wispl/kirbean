package me.wisp.kirbean.commands.music;

import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.audio.player.LoadHandler;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import me.wisp.kirbean.framework.annotations.Option;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.regex.Pattern;

public class Play implements SlashCommand {

    // regex to check if it is an youtube url
    private static final Pattern URL = Pattern.compile("^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+");

    @Command(name = "play", description = "Join a channel and play some music")
    @Option(name = "query", description = "link or search term")
    public void execute(SlashCommandInteractionEvent event) {
        String query = event.getOption("query").getAsString();
        GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
        GuildPlayer player = PlayerRepository.getPlayer(event.getGuild());

        if (!memberVoiceState.inAudioChannel()) {
            event.reply("You are not in a voice channel").queue();
            return;
        }

        if (!player.isInVoiceChannel()) {
            player.join(memberVoiceState.getChannel());
            player.setMessageChannelId(event.getMessageChannel().getIdLong());
        }

        if (memberVoiceState.getChannel().getIdLong() != player.getVoiceChannel()) {
            event.reply("You are not in the same voice channel as me").queue();
            return;
        }

        String search = URL.matcher(query).matches() ? query : "ytsearch: " + query;
        player.play(search, new LoadHandler(query, event, player));
    }
}
