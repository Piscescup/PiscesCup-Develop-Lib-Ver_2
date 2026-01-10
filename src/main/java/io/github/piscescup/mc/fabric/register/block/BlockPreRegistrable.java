package io.github.piscescup.mc.fabric.register.block;

import io.github.piscescup.mc.fabric.register.PreRegistrable;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;


/**
 * A pre-registration builder API for creating and registering a {@link Block}.
 *
 * <p>Implementations allow configuring the {@link AbstractBlock.Settings} to be used when
 * constructing the block and supplying a factory that will be invoked during the registration
 * phase to create the actual {@link Block} instance.
 *
 * <p>Methods return the builder instance to enable fluent chaining.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface BlockPreRegistrable extends PreRegistrable<BlockPostRegistrable> {
    /**
     * Configure the {@link AbstractBlock.Settings} that will be used when creating the {@link Block}.
     *
     * @param settings the block settings to apply; must not be null
     * @return this {@code BlockPreRegistrable} instance for method chaining
     * @see AbstractBlock.Settings
     * @throws NullPointerException if {@code settings} is null
     */
    BlockPreRegistrable settings(@NotNull AbstractBlock.Settings settings);

    /**
     * Provide a factory function that creates a {@link Block} given the configured {@link AbstractBlock.Settings}.
     *
     * <p>This is typically used for custom block implementations, e.g. {@code CustomBlock::new}.
     *
     * @param factory a function that accepts {@link AbstractBlock.Settings} and returns a {@link Block}; must not be null
     * @return this {@code BlockPreRegistrable} instance for method chaining
     * @throws NullPointerException if {@code factory} is null
     */
    BlockPreRegistrable factory(@NotNull Function<AbstractBlock.Settings, Block> factory);
}