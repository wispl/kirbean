package me.wisp.kirbean.commands.music;

import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.interaction.Interactivity;
import me.wisp.kirbean.interaction.pagination.Paginator;
import me.wisp.kirbean.interaction.pagination.impl.SimplePages;
import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
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
        Interactivity.createPaginator(event, new Paginator(pages));
    }
}
