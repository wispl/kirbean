package me.wisp.kirbean.framework.reflect;

import me.wisp.kirbean.framework.annotations.Choices;
import me.wisp.kirbean.framework.annotations.Command;
import me.wisp.kirbean.framework.annotations.Option;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public record CommandInfo(String name, String description, boolean isSubcommand, List<OptionInfo> parameters) {
    public static CommandInfo build(Method method) {
        Command command = method.getAnnotation(Command.class);
        List<OptionInfo> parameterDefinitions = new ArrayList<>();

        for (Annotation annotation : method.getDeclaredAnnotations()) {
            if (annotation instanceof Choices) {
                parameterDefinitions.add(OptionInfo.build((Choices) annotation));
            }
            else if (annotation instanceof Option) {
                parameterDefinitions.add(OptionInfo.build((Option) annotation));
            }
        }

        return new CommandInfo(
                command.name(),
                command.description(),
                command.subcommand(),
                parameterDefinitions);
    }

    public SlashCommandData toData() {
        return Commands.slash(name, description)
                .addOptions(parameters.stream()
                        .map(OptionInfo::toData)
                        .toList());
    }

    public SubcommandData toSubcommandData() {
        return new SubcommandData(name, description)
                .addOptions(parameters.stream()
                        .map(OptionInfo::toData)
                        .toList());
    }

}
