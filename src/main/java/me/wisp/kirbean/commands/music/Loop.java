package me.wisp.kirbean.commands.music;

import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Loop implements SlashCommand {
    @Command( name = "loop", description = "Loops the currently playing track")
    public void execute(SlashCommandInteractionEvent event) {
        GuildPlayer player = PlayerRepository.getOrCreatePlayer(event.getGuild());
        player.toggleLoop();
        event.reply("Set loop to " + player.getLoop() + ", it will take effect on the next track").queue();
    }
}
