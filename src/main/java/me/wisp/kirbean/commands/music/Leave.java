package me.wisp.kirbean.commands.music;

import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Leave implements SlashCommand {
    @Command( name = "leave", description = "Leaves the voice channel")
    public void execute(SlashCommandInteractionEvent event) {
        GuildPlayer player = PlayerRepository.getPlayer(event.getGuild());
        if (!player.isInVoiceChannel()) {
            event.reply("I am not in a voice channel...").queue();
            return;
        }

        player.leave();
        event.reply("Left the channel").queue();
    }
}
