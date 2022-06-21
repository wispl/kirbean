package me.wisp.kirbean.interaction.pagination;

import me.wisp.kirbean.interaction.Interactive;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class Paginator extends Interactive {
    private final Pages pages;

    public Paginator(Pages pages) {
        this.pages = pages;
    }

    @Override
    public MessageEmbed start() {
        return pages.renderPage();
    }

    @Override
    public void handle(ButtonInteractionEvent event) {
        String id = event.getButton().getId();
        String index = id.substring(id.indexOf(":") + 1);

        switch (index) {
            case "RIGHT" -> pages.nextPage();
            case "LEFT" -> pages.previousPage();
            case "STOP" -> {
                end();
                event.reply("Ending pagination").setEphemeral(true).queue();
                return;
            }
        }

        event.editMessageEmbeds(pages.renderPage()).queue();
    }
}
