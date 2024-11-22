package me.wisp.kirbean.commands.music;

import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Resume implements SlashCommand {
    @Command(name = "resume", description = "Resumes the player")
    public void execute(SlashCommandInteractionEvent event) {
        GuildPlayer player = PlayerRepository.getOrCreatePlayer(event.getGuild());
        if (player.isPaused()) {
            player.resume();
            return;
        }

        event.reply("Resumed the resumed player!").setEphemeral(true).queue();
    }
}
