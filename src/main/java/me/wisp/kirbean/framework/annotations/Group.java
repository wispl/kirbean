package me.wisp.kirbean.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.MODULE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Group {
    String name() default "";
    String groupDescription() default "No description";
}
