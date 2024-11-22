package me.wisp.kirbean.commands.music;

import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Pause implements SlashCommand {
    @Command(name = "pause", description = "Pauses the player")
    public void execute(SlashCommandInteractionEvent event) {
        GuildPlayer player = PlayerRepository.getOrCreatePlayer(event.getGuild());
        if (!player.isPaused()) {
            player.pause();
            return;
        }

        event.reply("Paused the paused player!").setEphemeral(true).queue();
    }
}
