package me.wisp.kirbean.interactive;

import me.wisp.kirbean.core.ext.TempListeners;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Paginator implements EventListener {
    private static final String PREV = "◀";
    private static final String STOP = "■";
    private static final String NEXT = "▶";

    private final List<MessageEmbed> pages;
    private int current = 0;

    public Paginator(List<MessageEmbed> pages) {
        this.pages = pages;
    }

    public void start(GenericCommandInteractionEvent event) {
        var id = UUID.randomUUID().toString();
        event.replyEmbeds(pages.get(current))
                .addActionRow(
                        Button.primary(id, PREV),
                        Button.danger(id, STOP),
                        Button.primary(id, NEXT)
                ).queue();
        TempListeners.addListener(Long.parseLong(id), this);
    }

    @Override
    public void onEvent(@NotNull GenericEvent e) {
        if (e instanceof ButtonInteractionEvent event) {
            switch (event.getButton().getLabel()) {
                case NEXT -> next();
                case PREV -> prev();
                case STOP -> {
                    List<LayoutComponent> components = event.getMessage()
                            .getComponents()
                            .stream()
                            .map(LayoutComponent::asDisabled)
                            .toList();

                    event.getMessage()
                            .editMessageComponents(components)
                            .queue();
                    return;
                }
            }
            event.getMessage().editMessageEmbeds(pages.get(current)).queue();
        }
    }

    private void next() {
        current++;
        if (current > pages.size()) {
            current = 0;
        }
    }

    private void prev() {
        current--;
        if (current < 0) {
            current = pages.size() - 1;
        }
    }
}