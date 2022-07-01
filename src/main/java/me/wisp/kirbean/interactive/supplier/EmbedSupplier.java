package me.wisp.kirbean.interactive.supplier;

import me.wisp.kirbean.framework.interactivity.Interactive;
import me.wisp.kirbean.framework.interactivity.Interactivity;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class EmbedSupplier implements Interactive {
    private final SupplierPage page;
    private static final ActionRow BUTTONS = ActionRow.of(
            Button.danger("supplier:CANCEL", "CANCEL"),
            Button.primary("supplier:REPLACE", "REPLACE"),
            Button.success("supplier:NEW", "NEW")
    );

    public EmbedSupplier(SupplierPage page) {
        this.page = page;
    }

    @Override public Message start() {
        return new MessageBuilder().setEmbeds(page.renderPage()).setActionRows(BUTTONS).build();
    }

    @Override public void onEvent(ButtonInteractionEvent event) {
        page.supply();
        switch (event.getButton().getId()) {
            case "supplier:REPLACE" -> event.editMessageEmbeds(page.renderPage()).queue();
            case "supplier:NEW" -> startNewSession(event);
            case "supplier:CANCEL" -> end(event);
        }
    }

    private void startNewSession(ButtonInteractionEvent event) {
        Interactivity.createInteractive(event.getTextChannel(), this);
        end(event);
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
