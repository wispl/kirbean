package me.wisp.kirbean.commands.image;

import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.core.annotations.Option;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class Avatar implements SlashCommand {
    @Command(name = "avatar", description = "Gets a user's avatar. No not the one from the Air Nomads")
    @Option(name = "user", description = "user's avatar to nab", type = OptionType.USER)
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getOption("user").getAsMember();
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle(member.getEffectiveName() + "'s avatar")
                .setImage(member.getEffectiveAvatarUrl());

        event.replyEmbeds(eb.build()).queue();
    }
}
