package me.wisp.kirbean.core.tree;

import net.dv8tion.jda.api.entities.MessageEmbed;

public interface DataNode<T> {
    T toData();
    String getName();
    String getDescription();
    MessageEmbed getHelp();
}
