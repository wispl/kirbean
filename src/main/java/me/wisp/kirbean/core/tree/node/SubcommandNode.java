package me.wisp.kirbean.core.tree.node;

import me.wisp.kirbean.core.reflect.CommandInfo;
import me.wisp.kirbean.core.tree.DataNode;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SubcommandNode implements DataNode<SubcommandData> {
    private final DataNode<?> parent;
    private final CommandInfo info;

    public SubcommandNode(CommandNode parent, CommandInfo info) {
        this.parent = parent;
        this.info = info;
    }

    public SubcommandNode(GroupNode parent, CommandInfo info) {
        this.parent = parent;
        this.info = info;
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
    public MessageEmbed getHelp() {
        return null;
    }

    @Override
    public SubcommandData toData() {
        return info.toSubcommandData();
    }
}
