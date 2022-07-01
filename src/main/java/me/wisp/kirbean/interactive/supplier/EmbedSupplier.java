package me.wisp.kirbean.interactivity.supplier;

import me.wisp.kirbean.interactivity.Interactive;
import me.wisp.kirbean.interactivity.Interactivity;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class EmbedSupplier extends Interactive {
    private final SupplierPage page;
    private static final ActionRow BUTTONS = ActionRow.of(
            Button.danger("supplier:CANCEL", "CANCEL"),
            Button.primary("supplier:REPLACE", "REPLACE"),
            Button.success("supplier:NEW", "NEW")
    );

    public EmbedSupplier(SupplierPage page) {
        this.page = page;
    }

    @Override
    public Message start() {
        return new MessageBuilder().setEmbeds(page.renderPage()).setActionRows(BUTTONS).build();
    }

    @Override
    public void onEvent(ButtonInteractionEvent event) {
        String id = event.getButton().getId();
        String index = id.substring(id.indexOf(":") + 1);

        switch (index) {
            case "REPLACE" -> page.supply();
            case "NEW" -> {
                page.supply();
                event.getChannel().sendMessage(this.start()).queue(m -> Interactivity.addTask(m, this));
                end();
                event.reply("Started new session").setEphemeral(true).queue();
                return;
            }
            case "CANCEL" -> {
                end();
                event.reply("Ending session").setEphemeral(true).queue();
                return;
            }
        }

        event.editMessageEmbeds(page.renderPage()).queue();
    }
}
