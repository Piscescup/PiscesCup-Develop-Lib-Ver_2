package io.github.piscescup.mc.fabric.command.annotation.argument;

import com.mojang.brigadier.arguments.ArgumentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Argument {
    String name();

    Class<? extends ArgumentType<?>> type();
}
