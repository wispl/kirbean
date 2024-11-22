package me.wisp.kirbean.interactive;

import me.wisp.kirbean.core.ext.TempListeners;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EmbedSupplier implements EventListener {
    private final static String STOP = "Stop";
    private final static String NEXT = "Next";

    private final EmbedBuilder builder = new EmbedBuilder();
    private final Supplier<String> supplier;

    public EmbedSupplier(Supplier<String> supplier) {
        this.supplier = supplier;
    }

    public EmbedSupplier(Supplier<String> supplier, MessageEmbed embed) {
        this.supplier = supplier;
        builder.copyFrom(embed);
    }

    public void start(SlashCommandInteractionEvent event) {
        var id = UUID.randomUUID().toString();
        event.replyEmbeds(image())
                .addActionRow(
                        Button.danger(id, STOP),
                        Button.primary(id, NEXT)
                ).queue();
        TempListeners.addListener(Long.parseLong(id), this);
    }

    @Override public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ButtonInteractionEvent e) {
            switch (e.getButton().getLabel()) {
                case NEXT -> e.editMessageEmbeds(image()).queue();
                case STOP -> {
                    List<LayoutComponent> components = e.getMessage()
                            .getComponents()
                            .stream()
                            .map(LayoutComponent::asDisabled)
                            .collect(Collectors.toList());

                    e.getMessage()
                            .editMessageComponents(components)
                            .queue();
                }
            }
        }
    }

    private MessageEmbed image() {
        return builder.setImage(supplier.get()).build();
    }
}