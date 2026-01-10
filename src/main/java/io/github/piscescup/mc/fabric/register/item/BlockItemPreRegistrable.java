package io.github.piscescup.mc.fabric.register.item;

import io.github.piscescup.mc.fabric.register.PreRegistrable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

/**
 * A pre-registration builder API for creating and registering an {@link Item} associated with a {@link Block}.
 *
 * <p>Implementations allow configuring the {@link Item.Settings} and supplying a factory that will be
 * invoked during the registration phase to construct the actual item instance.
 *
 * <p>Methods return the builder instance to enable fluent chaining.
 *
 * @author REN
 * @since 1.0.0
 */
public interface BlockItemPreRegistrable extends PreRegistrable<BlockItemPostRegistrable> {
    /**
     * Configure the {@link Item.Settings} to be used when creating the block item.
     *
     * @param settings The item settings to apply; must not be null.
     * @throws NullPointerException If {@code settings} is null.
     */
    BlockItemPreRegistrable setting(@NotNull Item.Settings settings);
    /**
     * Provide a factory that creates an {@link Item} from the given {@link Block} and {@link Item.Settings}.
     *
     * @param factory A bi-function accepting the {@link Block} and {@link Item.Settings} and returning an {@link Item}; must not be null
     * @throws NullPointerException If {@code factory} is null
     */
    BlockItemPreRegistrable factory(@NotNull BiFunction<Block, Item.Settings, Item> factory);
}
