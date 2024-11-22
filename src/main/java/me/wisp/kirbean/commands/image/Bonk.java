package me.wisp.kirbean.commands.image;

import me.wisp.kirbean.core.SlashCommand;
import me.wisp.kirbean.core.annotations.Command;
import me.wisp.kirbean.core.annotations.Option;
import me.wisp.kirbean.image.Coordinate;
import me.wisp.kirbean.image.MutableImage;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.FileUpload;

import java.awt.image.BufferedImage;

public class Bonk implements SlashCommand {
    private static final BufferedImage BONK = MutableImage.getImageFromFile("images/bonk.jpg");
    private static final Coordinate AVATAR = new Coordinate(860, 430);

    @Command(name = "bonk", description = "Sends a user to horny jail")
    @Option(name = "user", description = "The vigilante to incarcerate", type = OptionType.USER)
    public void execute(SlashCommandInteractionEvent event) {
        MutableImage image = new MutableImage(BONK);
        MutableImage avatar = new MutableImage(event.getOption("user").getAsMember().getEffectiveAvatarUrl());

        image.drawImage(avatar.setScale(2).setRound(), AVATAR);
        event.replyFiles(FileUpload.fromData(image.asInputStream("jpg"), "bonk.jpg")).queue();
    }
}
