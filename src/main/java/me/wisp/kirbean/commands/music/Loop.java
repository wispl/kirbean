package me.wisp.kirbean.commands.music;

import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Loop implements SlashCommand {
    @Command( name = "loop", description = "Loops the currently playing track")
    public void execute(SlashCommandInteractionEvent event) {
        GuildPlayer player = PlayerRepository.getPlayer(event.getGuild());
        if (player.isIdle()) {
            event.reply("Nothing is playing right now... you brain must be loopy").queue();
            return;
        }

        player.toggleLoop();
        event.reply("Set loop to " + player.getLoop() + ", it will take effect on the next track").queue();
    }
}
