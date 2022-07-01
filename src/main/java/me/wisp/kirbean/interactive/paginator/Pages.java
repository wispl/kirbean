package me.wisp.kirbean.interactive.pagination;

import net.dv8tion.jda.api.entities.MessageEmbed;

public interface Pages {
    void previousPage();
    void nextPage();
    MessageEmbed renderPage();
}
