package me.wisp.kirbean.commands.music;

import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Unshuffle implements SlashCommand {
    @Command( name = "unshuffle", description = "Unshuffles the queue")
    public void execute(SlashCommandInteractionEvent event) {
        GuildPlayer player = PlayerRepository.getPlayer(event.getGuild());
        if (player.isQueueEmpty()) {
            event.reply("Queue is empty, just like your head apparently...").queue();
            return;
        }

        player.unshuffleQueue();
        event.reply("Queue has been unshuffled!").queue();
    }
}
