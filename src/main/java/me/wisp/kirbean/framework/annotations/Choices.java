package me.wisp.kirbean.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Choices {
    String name() default "";
    String description() default "No description";
    String[] choices();
    boolean isRequired() default true;
}
