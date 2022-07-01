package me.wisp.kirbean.commands.music;

import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import me.wisp.kirbean.framework.interactivity.Interactivity;
import me.wisp.kirbean.interactive.paginator.Paginator;
import me.wisp.kirbean.interactive.paginator.impl.SimplePages;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Queue implements SlashCommand {
    @Command(name = "queue", description = "Gets the playing queue")
    public void execute(SlashCommandInteractionEvent event) {
        GuildPlayer player = PlayerRepository.getPlayer(event.getGuild());
        if (player.isQueueEmpty()) {
            event.reply("Queue is empty, just like your head apparently...").queue();
            return;
        }

        SimplePages pages = new SimplePages("Currently playing " + player.getCurrent().getInfo().title, player.getQueue());
        Interactivity.createInteractive(event, new Paginator(pages));
    }
}
