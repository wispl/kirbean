package me.wisp.kirbean.framework.help;

import me.wisp.kirbean.framework.reflect.CommandInfo;
import me.wisp.kirbean.framework.tree.CommandTree;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

public class HelpMessageFactory {
    private final CommandTree tree;
    private final MessageEmbed generalEmbed;

    public HelpMessageFactory(CommandTree tree) {
        this.tree = tree;
        this.generalEmbed = createGeneralHelp();
    }

    public MessageEmbed getHelpForCommand(String name) {
        if (name == null) return generalEmbed;
        return tree.findCommand(name).getHelp();
    }

    private MessageEmbed createGeneralHelp() {
        EmbedBuilder builder = new EmbedBuilder().setTitle("Helping Around").setDescription("Categories");
        tree.getCommandsByCategory().forEach((key, value) -> builder.addField(key, processCommands(value), true));
        return builder.build();
    }

    private String processCommands(List<CommandInfo> list) {
       StringBuilder builder = new StringBuilder();
       list.stream().map(CommandInfo::name).toList().forEach(n -> builder.append(n).append("\n"));
       return builder.toString();
    }
}
