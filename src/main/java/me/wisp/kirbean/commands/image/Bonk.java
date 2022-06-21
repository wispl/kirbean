package me.wisp.kirbean.commands.image;

import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import me.wisp.kirbean.framework.annotations.Option;
import me.wisp.kirbean.utils.Coordinate;
import me.wisp.kirbean.utils.Image;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Bonk implements SlashCommand {
    private static final BufferedImage BONK = Image.fromFile(new File("src/main/java/me/wisp/kirbean/assets/bonk.png"));
    private static final Coordinate AVATAR = new Coordinate(860, 430);

    @Command(name = "bonk", description = "Sends a user to horny jail")
    @Option(name = "user", description = "The vigilante to incarcerate", type = OptionType.USER)
    public void execute(SlashCommandInteractionEvent event) {
        BufferedImage image = Image.clone(BONK);
        BufferedImage avatar = Image.fromURL(event.getOption("user").getAsMember().getEffectiveAvatarUrl());
        avatar = Image.round(Image.scale(avatar, 2));

        Graphics2D imageGraphics = image.createGraphics();
        imageGraphics.drawImage(avatar, AVATAR.x(), AVATAR.y(), null);
        imageGraphics.dispose();

        event.replyFile(Image.asByteArrayStream(image, "png"), "bonk.png").queue();
    }
}
