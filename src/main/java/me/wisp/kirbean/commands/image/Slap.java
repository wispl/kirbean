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

public class Slap implements SlashCommand {

    private static final BufferedImage SLAP = MutableImage.getImageFromFile("images/slap.jpg");
    private static final Coordinate AVATAR = new Coordinate(270, 180);
    private static final Coordinate USER_AVATAR = new Coordinate(720, 85);

    @Command(name = "slap", description = "Slaps someone for being a blabbering baboon")
    @Option(name = "user", description = "the idiot to slap", type = OptionType.USER)
    public void execute(SlashCommandInteractionEvent event) {
        MutableImage image = new MutableImage(SLAP);
        MutableImage avatar = new MutableImage(event.getOption("user").getAsMember().getEffectiveAvatarUrl());
        MutableImage userAvatar = new MutableImage(event.getMember().getEffectiveAvatarUrl());

        image.drawImage(avatar.setScale(3).setRound(), AVATAR);
        image.drawImage(userAvatar.setScale(2).setRound(), USER_AVATAR);

        event.replyFiles(FileUpload.fromData(image.asInputStream("jpg"), "slap.jpg")).queue();
    }
}
