package me.wisp.kirbean.framework.annotations;

import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Options.class)
public @interface Option {
    String name() default "";
    String description() default "No description";
    OptionType type() default OptionType.STRING;
    boolean isRequired() default true;
}
