package io.github.piscescup.mc.fabric.command.arguments;

import com.mojang.brigadier.arguments.ArgumentType;

/**
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface ArgumentAdapter<T, ARG extends ArgumentType<T>, ARG_ADAPTER extends ArgumentAdapter<T, ARG, ARG_ADAPTER>> {
}
