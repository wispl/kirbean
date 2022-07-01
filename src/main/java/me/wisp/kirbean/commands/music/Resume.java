package me.wisp.kirbean.commands.music;

import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Resume implements SlashCommand {
    @Command(name = "resume", description = "Resumes the player")
    public void execute(SlashCommandInteractionEvent event) {
        GuildPlayer player = PlayerRepository.getPlayer(event.getGuild());
        if (player.isIdle()) {
            event.reply("Queue is empty or nothing is playing").queue();
            return;
        }

        if (!player.isPaused()) {
            event.reply("The player is not paused. Pause your stupidity").queue();
            return;
        }

        player.resume();
        event.reply("Successfully resumed the player").queue();
    }
}
