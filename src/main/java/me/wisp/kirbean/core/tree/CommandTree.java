package me.wisp.kirbean.core.tree;

import me.wisp.kirbean.core.reflect.CommandInfo;
import me.wisp.kirbean.core.tree.node.CommandNode;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandTree {

    private final Map<String, CommandNode> nodes = new HashMap<>();
    private final Map<String, List<CommandInfo>> category = new HashMap<>();

    public CommandTree() {}

    public DataNode<?> findCommand(String name) {
        return nodes.get(name);
    }

    public void addCommand(String module, CommandInfo info) {
        nodes.put(info.name(), new CommandNode(info));
        getCategory(module).add(info);
    }

    private List<CommandInfo> getCategory(String category) {
        return this.category.computeIfAbsent(category, m -> new ArrayList<>());
    }

    public List<SlashCommandData> toData() {
        return nodes.values().stream().map(DataNode::toData).collect(Collectors.toList());
    }

    public List<String> getCommands() {
        return nodes.keySet().stream().toList();
    }

    public Map<String, List<CommandInfo>> getCommandsByCategory() {
        return category;
    }
}