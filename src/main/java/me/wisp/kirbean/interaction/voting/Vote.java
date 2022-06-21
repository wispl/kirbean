package me.wisp.kirbean.interaction.voting;

import me.wisp.kirbean.interaction.Interactive;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class Vote extends Interactive {
    private final Predicate<Member> check;
    private final VotePage page;
    private final Runnable onSuccess;
    private final Set<Long> alreadyVoted;

    public Vote(Predicate<Member> check, VotePage page, Runnable onSuccess) {
        this.check = check;
        this.page = page;
        this.onSuccess = onSuccess;

        this.alreadyVoted = new HashSet<>();
    }

    @Override
    public MessageEmbed start() {
        return page.renderPage();
    }

    @Override
    public void handle(ButtonInteractionEvent event) {
        String id = event.getButton().getId();
        String index = id.substring(id.indexOf(":") + 1);

        if (!check.test(event.getMember())) {
            event.reply("You do not match the requirements to vote").queue();
            return;
        }

        if (alreadyVoted.contains(event.getMember().getIdLong())) {
            event.reply("You have already voted").setEphemeral(true).queue();
            return;
        }
        alreadyVoted.add(event.getMember().getIdLong());

        switch (index) {
            case "YES" -> page.addVote();
            case "NO" -> page.removeVote();
        }

        event.editMessageEmbeds(page.renderPage()).queue();

        if (page.isGoalReached()) {
            onSuccess.run();
            end();
        }
    }

    // no-op, voting should not be renewed
    @Override
    public void renew() {
    }
}
