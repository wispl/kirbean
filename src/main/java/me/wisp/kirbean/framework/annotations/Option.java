package me.wisp.kirbean.framework.annotations;

import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Option {
    String name() default "";
    String description() default "No description";
    OptionType type() default OptionType.STRING;
    boolean isRequired() default true;
}
