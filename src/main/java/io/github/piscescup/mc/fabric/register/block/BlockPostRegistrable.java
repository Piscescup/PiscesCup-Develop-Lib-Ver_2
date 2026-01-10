package io.github.piscescup.mc.fabric.register.block;

import io.github.piscescup.mc.fabric.register.PostRegistrable;
import net.minecraft.block.Block;


/**
 * Represents a post-registration handler for a Minecraft {@link Block}.
 *
 * <p>This interface defines behavior that is executed <strong>after</strong>
 * a {@link Block} has been registered. It is typically used to perform additional
 * setup, linking, or deferred logic that depends on the completed block
 * registration process.
 *
 * <p>Implementations are expected to integrate with {@link BlockRegister}
 * and participate in the post-registration lifecycle in a type-safe and fluent
 * manner.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface BlockPostRegistrable
    extends PostRegistrable<Block, BlockPostRegistrable, BlockRegister>
{
}
