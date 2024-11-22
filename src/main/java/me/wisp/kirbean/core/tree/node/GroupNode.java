package me.wisp.kirbean.core.tree.node;

import me.wisp.kirbean.core.reflect.CommandInfo;
import me.wisp.kirbean.core.tree.DataNode;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.util.LinkedList;
import java.util.List;

public class GroupNode implements DataNode<SubcommandGroupData> {
    private final CommandNode parent;
    private final List<SubcommandNode> subcommands = new LinkedList<>();

    private final String name;
    private final String description;

    public GroupNode(CommandNode parent, String name, String description) {
        this.parent = parent;
        this.name = name;
        this.description = description;
    }

    public void addSubcommand(CommandInfo info) {
       subcommands.add(new SubcommandNode(this, info));
    }

    @Override
    public SubcommandGroupData toData() {
       return new SubcommandGroupData(name, description)
               .addSubcommands(subcommands.stream().map(SubcommandNode::toData).toList());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public MessageEmbed getHelp() {
        return null;
    }
}