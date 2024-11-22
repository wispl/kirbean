package me.wisp.kirbean.core;

import me.wisp.kirbean.core.tree.CommandTree;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommandUpdater {
    private static final Logger logger = LoggerFactory.getLogger(CommandUpdater.class);
    private final List<SlashCommandData> data;

    public CommandUpdater(CommandTree tree) {
        this.data = tree.toData();
        logger.info(data.size() + " SlashCommands found by updater!");
        var optionData = new OptionData(OptionType.STRING, "command", "name of command")
                .setAutoComplete(true);
        SlashCommandData helpCommand = Commands.slash("help", "Get some help")
                .addOptions(optionData);
        data.add(helpCommand);
    }

    public void purgeAndUpdateCommands(Guild guild) {
        removeCommands(guild);
        updateCommands(guild);
    }

    public void purgeAndUpdateCommands(JDA jda) {
        removeCommands(jda);
        updateCommands(jda);
    }

    public void updateCommands(JDA jda) {
        jda.updateCommands().addCommands(data).queue();
        logger.info("Updated commands globally");
    }

    public void updateCommands(Guild guild) {
        guild.updateCommands().addCommands(data).queue();
        logger.info("Updated commands locally for guild " + guild.getName());
    }

    public void removeCommands(JDA jda) {
        jda.updateCommands().queue();
        logger.info("Removed all commands globally");
    }

    public void removeCommands(Guild guild) {
        guild.updateCommands().queue();
        logger.info("Removed all commands from " + guild.getName());
    }
}