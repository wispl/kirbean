package me.wisp.kirbean.framework.tree.node;

import me.wisp.kirbean.framework.reflect.CommandInfo;
import me.wisp.kirbean.framework.reflect.OptionInfo;
import me.wisp.kirbean.framework.tree.DataNode;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.LinkedList;
import java.util.List;

public class CommandNode implements DataNode<SlashCommandData> {
    private final CommandInfo info;
    private final List<SubcommandNode> subcommands = new LinkedList<>();
    private final List<GroupNode> groups = new LinkedList<>();

    private final MessageEmbed help;

    public CommandNode(CommandInfo info) {
        this.info = info;
        help = createHelp();
    }

    public void addSubcommand(CommandInfo info) {
        subcommands.add(new SubcommandNode(this, info));
    }

    public void addGroup(String name, String description) {
        groups.add(new GroupNode(this, name, description));
    }

    @Override
    public String getName() {
        return info.name();
    }

    @Override
    public String getDescription() {
        return info.description();
    }

    @Override
    public SlashCommandData toData() {
        return info.toData()
                .addSubcommands(subcommands.stream().map(DataNode::toData).toList())
                .addSubcommandGroups(groups.stream().map(DataNode::toData).toList());
    }

    private MessageEmbed createHelp() {
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("Help for... " + info.name())
                .setDescription(info.description());
        StringBuilder stringBuilder = new StringBuilder().append("/").append(info.name()).append(" ");
        for (OptionInfo parameter : info.parameters()) {
            builder.addField(parameter.formatName(), parameter.description(), false);
            stringBuilder.append(parameter.formatName()).append(" ");
        }
        builder.addField("Usage", stringBuilder.toString(), false);
        builder.setFooter("Parameters encased in brackets means they are required");
        return builder.build();
    }

    public MessageEmbed getHelp() {
        return help;
    }
}
