package me.wisp.kirbean.interaction.supplier;

import me.wisp.kirbean.interaction.Buttons;
import me.wisp.kirbean.interaction.Interactive;
import me.wisp.kirbean.interaction.Interactivity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class EmbedSupplier extends Interactive {
    private final SupplierPage page;

    public EmbedSupplier(SupplierPage page) {
        this.page = page;
    }

    @Override
    public MessageEmbed start() {
        return page.renderPage();
    }

    @Override
    public void handle(ButtonInteractionEvent event) {
        String id = event.getButton().getId();
        String index = id.substring(id.indexOf(":") + 1);

        switch (index) {
            case "REPLACE" -> page.supply();
            case "NEW" -> {
                page.supply();
                event.getChannel().sendMessageEmbeds(this.start()).setActionRows(Buttons.createDefaultSupplier())
                        .queue(m -> Interactivity.addTask(m, this));
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
