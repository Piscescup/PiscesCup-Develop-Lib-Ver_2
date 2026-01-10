package io.github.piscescup.mc.fabric.register.item;

import io.github.piscescup.mc.fabric.register.PreRegistrable;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Represents a pre-registration builder for registering a Minecraft {@link Item}.
 *
 * <p>This interface provides configuration methods to supply {@link Item.Settings} and a factory
 * function that will be used to construct the actual {@link Item} during the registration phase.
 *
 * <p>Implementations should return the same builder instance to allow fluent chaining of calls.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface ItemPreRegistrable
    extends PreRegistrable<ItemPostRegistrable>
{
    /**
     * Configure the {@link Item.Settings} that will be passed to the item factory when creating the item.
     *
     * @param settings The item settings to apply; must not be null
     * @throws NullPointerException If {@code settings} is null
     */
    ItemPreRegistrable setting(@NotNull Item.Settings settings);


    /**
     * Provide a factory function that creates an {@link Item} given the configured {@link Item.Settings}.
     *
     * <p>The function is invoked during the registration step with the effective settings and should
     * return a new {@link Item} instance.
     *
     * @param factory A function that accepts {@link Item.Settings} and returns an {@link Item}; must not be null
     * @throws NullPointerException If {@code factory} is null
     */
    ItemPreRegistrable factory(@NotNull Function<Item.Settings, Item> factory);

}
