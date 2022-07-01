package me.wisp.kirbean.polls;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.time.Instant;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class SimplePoll implements Poll {

    private final PollPage page;
    private final long duration;
    private final TimeUnit unit;

    private final HashSet<Long> votedAlready = new HashSet<>();
    private static final ActionRow buttons = ActionRow.of(
            Button.success("poll:YES", "Yes"),
            Button.danger("poll:NO", "No")
    );

    public SimplePoll(PollPage page, long duration, TimeUnit unit) {
        this.page = page;
        this.duration = duration;
        this.unit = unit;
    }

    public Message start() {
        return new MessageBuilder()
                .setEmbeds(new EmbedBuilder()
                        .setAuthor(page.getAuthor())
                        .setThumbnail(page.getAuthorAvatar())
                        .setTitle(page.getTitle())
                        .setDescription(page.getQuestion())
                        .setFooter("The poll lasts for " + duration + " " + unit.toString())
                        .setTimestamp(Instant.now())
                        .build())
                .setActionRows(buttons)
                .build();
    }

    public void onEvent(GenericComponentInteractionCreateEvent event) {
        if (!(event instanceof ButtonInteractionEvent)) {
            return;
        }

        long memberId = event.getMember().getIdLong();
        if (votedAlready.contains(memberId)) {
            event.reply("You have already voted").setEphemeral(true).queue();
            return;
        }
        votedAlready.add(memberId);

        switch (event.getComponentId()) {
            case "poll:YES" -> page.addVote();
            case "poll:NO" -> page.removeVote();
        }
        event.reply("Response received!").setEphemeral(true).queue();
    }


    public Message end(Message message) {
        return new MessageBuilder().setEmbeds(
                new EmbedBuilder()
                        .setTitle("This poll has ended!")
                        .setDescription("Results: " + page.getVotes())
                        .addField(page.getTitle(), page.getQuestion(), false)
                        .addField("Reference", message.getJumpUrl(), false)
                        .build())
                .build();
    }

}
