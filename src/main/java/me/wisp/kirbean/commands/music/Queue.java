package me.wisp.kirbean.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.wisp.kirbean.audio.PlayerRepository;
import me.wisp.kirbean.audio.player.GuildPlayer;
import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.interactive.Paginator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;
import java.util.List;

public class Queue implements SlashCommand {

    private static final int TRACKS_PER_PAGE = 15;
    @Command(name = "queue", description = "Gets the playing queue")
    public void execute(SlashCommandInteractionEvent event) {
        GuildPlayer player = PlayerRepository.getPlayer(event.getGuild());
        if (player.isQueueEmpty()) {
            event.reply("Nothing is playing right now...").queue();
            return;
        }
        List<MessageEmbed> embeds = new ArrayList<>();
        var builder = new EmbedBuilder();
        java.util.Queue<AudioTrack> queue = player.getQueue();


        // author is at the top of the embed so we use it to set the actual title
        builder.setAuthor("Queue")
                .setTitle("Currently Playing " + player.getCurrent().getInfo().title);

        int page = 1;
        int trackCount = 1;
        int totalPages = queue.size() / TRACKS_PER_PAGE;
        for (final var track : queue) {
            builder.appendDescription("```\n")
                    .appendDescription(String.valueOf(trackCount))
                    .appendDescription(": ")
                    .appendDescription(track.getInfo().title)
                    .appendDescription("\n```");

            if (trackCount % TRACKS_PER_PAGE == 0 || trackCount == queue.size()) {
                builder.setFooter(page + "/" + totalPages);
                embeds.add(builder.build());
                builder.setDescription("");
            }
            trackCount++;
        }

        new Paginator(embeds).start(event);
    }
}
