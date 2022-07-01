package me.wisp.kirbean.commands.music;

import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Pause implements SlashCommand {
    @Command(name = "pause", description = "Pauses the player")
    public void execute(SlashCommandInteractionEvent event) {
        GuildPlayer player = PlayerRepository.getPlayer(event.getGuild());
        if (player.isIdle()) {
            event.reply("Queue is empty or nothing is playing").queue();
            return;
        }
        if (player.isPaused()) {
            event.reply("Player is already paused, you crab with dementia").queue();
            return;
        }

        player.pause();
        event.reply("Player successfully paused").queue();
    }
}
