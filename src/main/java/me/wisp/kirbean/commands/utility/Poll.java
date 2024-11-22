package me.wisp.kirbean.commands.utility;

import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Choices;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.core.annotations.Option;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

    /*
public class Poll implements SlashCommand {
    private static final Modal MODAL = Modal.create("poll:modal", "Poll Creation")
            .addActionRow(
                    TextInput.create("poll:modal:title", "Title", TextInputStyle.SHORT)
                            .setRequired(true)
                            .build())
            .addActionRow(
                    TextInput.create("poll:modal:question", "Question", TextInputStyle.PARAGRAPH)
                            .setRequired(true)
                            .build())
            .build();
    @Command(name = "poll", description = "Does a poll")
    @Option(name = "duration", description = "duration of the poll", type = OptionType.INTEGER)
    @Choices(name = "unit", description = "unit of time", choices = {"minutes", "hours", "days"})
    public void execute(SlashCommandInteractionEvent event) {
        List<String> responses;
        try {
            responses = Collector.createModal(event, MODAL).get();
        } catch (InterruptedException | ExecutionException e) {
            event.reply("Error creating poll").setEphemeral(true).queue();
            return;
        }

        String title = responses.get(0);
        String question = responses.get(1);
        int duration = event.getOption("duration").getAsInt();
        TimeUnit unit = TimeUnit.valueOf(event.getOption("unit").getAsString().toUpperCase());
        Member member = event.getMember();
        PollPage page = new PollPage(member.getEffectiveName(), member.getEffectiveAvatarUrl(), title, question);

        Polls.createPoll(event.getTextChannel(), page, duration, unit);
    }

}
     */
