package me.wisp.kirbean.framework.reflect;

import me.wisp.kirbean.framework.annotations.Choices;
import me.wisp.kirbean.framework.annotations.Option;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Arrays;
import java.util.List;

public record OptionInfo(OptionType type, String name, String description, boolean isRequired, List<String> choices) {
    public static OptionInfo build(Choices option) {
        return new OptionInfo(
                OptionType.STRING,
                option.name(),
                option.description(),
                option.isRequired(),
                Arrays.stream(option.choices()).toList());
    }
    public static OptionInfo build(Option option) {
        return new OptionInfo(
                option.type(),
                option.name(),
                option.description(),
                option.isRequired(),
                null
        );
    }

    public OptionData toData() {
        OptionData data = new OptionData(type, name, description, isRequired);
        if (choices != null) {
            for (String choice : choices) {
                data.addChoice(choice, choice);
            }
        }
        return data;
    }

    public String formatName() {
        return isRequired ? "[" + name + "]" : name;
    }
}
