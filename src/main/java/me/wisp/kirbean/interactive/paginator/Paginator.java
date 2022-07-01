package me.wisp.kirbean.interactive.paginator;

import me.wisp.kirbean.framework.interactivity.Interactive;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class Paginator implements Interactive {
    private final Pages pages;
    private static final ActionRow BUTTONS = ActionRow.of(
            Button.primary("paginator:LEFT", "◀"),
            Button.danger("paginator:STOP", "■"),
            Button.primary("paginator:RIGHT", "▶")
    );

    public Paginator(Pages pages) {
        this.pages = pages;
    }

    @Override public Message start() {
        return new MessageBuilder().setEmbeds(pages.renderPage()).setActionRows(BUTTONS).build();
    }

    @Override public void onEvent(ButtonInteractionEvent event) {
        switch (event.getButton().getId()) {
            case "paginator:RIGHT" -> pages.nextPage();
            case "paginator:LEFT" -> pages.previousPage();
            case "paginator:STOP" -> {
                end(event);
                return;
            }
        }

        event.editMessageEmbeds(pages.renderPage()).queue();
    }

    private void end(ButtonInteractionEvent event) {
        event.deferEdit()
                .setActionRows(
                        event.getMessage()
                                .getActionRows()
                                .stream()
                                .map(ActionRow::asDisabled)
                                .toList())
                .queue();
    }
}