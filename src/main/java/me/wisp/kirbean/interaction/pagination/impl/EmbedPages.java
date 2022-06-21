package me.wisp.kirbean.interaction.pagination.impl;

import me.wisp.kirbean.interaction.pagination.Pages;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

public class EmbedPages implements Pages {
    private final List<MessageEmbed> pages;
    private final int size;
    private int current = 0;

    public EmbedPages(List<MessageEmbed> pages) {
        this.pages = pages;
        this.size = pages.size();
    }

    public void previousPage() {
        current = current == 0 ? size - 1 : current - 1;
    }

    public void nextPage() {
        current = current == size-1 ? 0 : current + 1;
    }

    public MessageEmbed renderPage() {
        return pages.get(current);
    }
}
