package me.wisp.kirbean.commands.fun;

import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.core.annotations.Option;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.concurrent.ThreadLocalRandom;

public class Eightball implements SlashCommand {
    private static final String[] ANSWERS = {
            "It is certain.",
            "It is decidedly so.",
            "Without a doubt.",
            "Yes definitely.",
            "You may rely on it.",
            "As I see it, yes.",
            "Most likely.",
            "Outlook good.",
            "Yes.",
            "Signs point to yes.",
            "Reply hazy, try again.",
            "Ask again later.",
            "Better not tell you now.",
            "Cannot predict now.",
            "Concentrate and ask again.",
            "Don't count on it.",
            "My reply is no.",
            "My sources say no.",
            "Outlook not so good.",
            "Very doubtful."
    };

    @Command( name = "8ball", description = "Ask the mighty 8ball a question" )
    @Option( name = "question", description = "Ask your worries away..." )
    public void execute(SlashCommandInteractionEvent event) {
        String answer = ANSWERS[ThreadLocalRandom.current().nextInt(ANSWERS.length)];

        var builder = new EmbedBuilder()
                .setTitle("Question: " + event.getOption("question").getAsString())
                .setDescription("●:\n" + answer);
        event.replyEmbeds(builder.build()).queue();
    }
}
