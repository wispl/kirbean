package me.wisp.kirbean.commands.utility;

import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import me.wisp.kirbean.framework.annotations.Option;
import me.wisp.kirbean.interaction.Interactivity;
import me.wisp.kirbean.interaction.voting.Vote;
import me.wisp.kirbean.interaction.voting.VotePage;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Poll implements SlashCommand {
    @Command(name = "poll", description = "Does a poll")
    @Option(name = "question", description = "poll question to ask")
    public void execute(SlashCommandInteractionEvent event) {
        VotePage page = new VotePage(event.getMember().getEffectiveName() + " created a poll!",
                "```\n" + event.getOption("name").getAsString() + "\n```",
                Integer.MAX_VALUE); // for now
        Interactivity.createVote(event, new Vote((m) -> true, page, () -> event.getHook().sendMessage("yay!").queue()));
    }
}
