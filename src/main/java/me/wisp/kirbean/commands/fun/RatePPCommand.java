package me.wisp.kirbean.commands.fun;

import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.core.annotations.Option;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.concurrent.ThreadLocalRandom;

public class RatePPCommand implements SlashCommand {
    @Command(name = "pp", description = "Rates a person's pp, yes this is legal, maybe...")
    @Option(name = "target", description = "person to perform a prostrate exam on...",
            isRequired = false,
            type = OptionType.USER)
    public void execute(SlashCommandInteractionEvent event) {
        Member target = event.getOption("target",
                event.getMember(),
                OptionMapping::getAsMember);

        int size = ThreadLocalRandom.current().nextInt(0, 10);
        String text = size == 0 ? "is Nonexistent" : "is\n8" + "=".repeat(size) + "D";
        event.reply(target.getAsMention() + "'s pp " + text).queue();
    }
}
