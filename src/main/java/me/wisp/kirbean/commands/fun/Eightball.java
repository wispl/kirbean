package me.wisp.kirbean.commands.fun;

import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import me.wisp.kirbean.framework.annotations.Option;
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
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle("Question: " + event.getOption("question").getAsString())
                .setDescription("●:\n" + ANSWERS[ThreadLocalRandom.current().nextInt(ANSWERS.length)]);

        event.replyEmbeds(eb.build()).queue();
    }
}
