package me.wisp.kirbean.commands.image;

import me.wisp.kirbean.framework.SlashCommand;
import me.wisp.kirbean.framework.annotations.Command;
import me.wisp.kirbean.framework.annotations.Option;
import me.wisp.kirbean.utils.Image;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Slap implements SlashCommand {

    private static final BufferedImage SLAP = Image.fromFile(new File("src/main/java/me/wisp/kirbean/assets/slap.jpg"));

    @Command(name = "slap", description = "Slaps someone for being a blabbering baboon")
    @Option(name = "user", description = "the idiot to slap", type = OptionType.USER)
    public void execute(SlashCommandInteractionEvent event) {
        BufferedImage image = Image.clone(SLAP);
        BufferedImage avatar = Image.fromURL(event.getOption("user").getAsMember().getEffectiveAvatarUrl());
        avatar = Image.round(Image.scale(avatar, 3));
        BufferedImage userAvatar = Image.fromURL(event.getMember().getEffectiveAvatarUrl());
        userAvatar = Image.round(Image.scale(userAvatar, 2));

        Graphics2D graphics = image.createGraphics();
        graphics.drawImage(avatar, 270, 180, null);
        graphics.drawImage(userAvatar, 720, 85, null);
        graphics.dispose();

        event.replyFile(Image.asByteArrayStream(image, "jpg"), "slap.jpg").queue();
    }
}
