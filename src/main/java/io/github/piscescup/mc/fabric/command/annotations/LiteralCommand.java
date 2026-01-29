package io.github.piscescup.mc.fabric.command.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
@Target( ElementType.TYPE )
@Retention(RetentionPolicy.RUNTIME)
public @interface LiteralCommand {
    String value();

    String[] alias() default {};
}
