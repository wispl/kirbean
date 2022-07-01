package me.wisp.kirbean.interactive.pagination.impl;

import me.wisp.kirbean.interactive.pagination.Pages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

public class SimplePages implements Pages {
    private final EmbedBuilder builder = new EmbedBuilder();

    private final List<String> pages;
    private final int size;
    private int current = 0;

    public SimplePages(String author, String title, String url, List<String> pages) {
        this(title, url, pages);
        builder.setAuthor(author);
    }

    public SimplePages(String title, String url, List<String> pages) {
        this(pages);
        builder.setTitle(title, url);
    }

    public SimplePages(String title, List<String> pages) {
        this(pages);
        builder.setTitle(title);
    }
    public SimplePages(List<String> pages) {
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
        return builder.setDescription(pages.get(current)).setFooter(current+1 + "/" + size).build();
    }
}
