package me.wisp.kirbean.interaction.supplier;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.function.Supplier;

public class SupplierPage {
    private final EmbedBuilder builder;

    private final Supplier<String> supplier;
    private final boolean isImage;

    private String current;

    public SupplierPage(String title, Supplier<String> supplier, String footer) {
        this.builder = new EmbedBuilder()
                .setTitle(title)
                .setFooter(footer);

        this.supplier = supplier;
        supply();
        isImage = true;
    }

    public SupplierPage(String title, Supplier<String> supplier, String footer, boolean isImage) {
        this.builder = new EmbedBuilder()
                .setTitle(title)
                .setFooter(footer);

        this.supplier = supplier;
        supply();

        this.isImage = isImage;
    }

    public void supply() {
        current = supplier.get();
    }

    public MessageEmbed renderPage() {
        return isImage ? builder.setImage(current).build() : builder.setDescription(current).build();
    }
}
