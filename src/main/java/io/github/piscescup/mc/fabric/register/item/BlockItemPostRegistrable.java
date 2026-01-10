package io.github.piscescup.mc.fabric.register.item;

import io.github.piscescup.mc.fabric.register.PostRegistrable;
import net.minecraft.item.Item;

/**
 * Represents a post-registration handler for a Minecraft {@link Item} that
 * is associated with a {@code Block} (i.e. a BlockItem).
 *
 * <p>This interface defines behavior that is executed <strong>after</strong>
 * a block item {@link Item} has been registered. It is commonly used to perform
 * additional setup specific to block-backed items, such as linking the item
 * to its block, configuring rendering behavior, or applying deferred logic
 * that depends on successful registration.
 *
 * <p>Implementations are expected to integrate with {@link BlockItemRegister}
 * and participate in the post-registration lifecycle in a type-safe and fluent
 * manner.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface BlockItemPostRegistrable
    extends PostRegistrable<Item, BlockItemPostRegistrable, BlockItemRegister>
{
}
