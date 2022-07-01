package me.wisp.kirbean.commands.music;

import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Nowplaying implements SlashCommand {
    @Command(name = "nowplaying", description = "Gets information of the current track")
    public void execute(SlashCommandInteractionEvent event) {
        GuildPlayer player = PlayerRepository.getPlayer(event.getGuild());
        if (player.isIdle()) {
            event.reply("No audio playing right now :(").queue();
            return;
        }

        event.replyEmbeds(player.getNowPlaying()).queue();
    }
}
