package io.github.piscescup.mc.fabric.register.itemgroup;

import io.github.piscescup.mc.fabric.register.PostRegistrable;
import net.minecraft.item.ItemGroup;

/**
 * Represents a post-registration handler for a Minecraft {@link ItemGroup}.
 *
 * <p>This interface defines behavior that is executed <strong>after</strong> an
 * {@link ItemGroup} has been registered. It is typically used to perform additional
 * setup, linking, or deferred logic that depends on the completed registration process.
 *
 * <p>Implementations are expected to integrate with {@link ItemGroupRegister} and
 * participate in the post-registration lifecycle in a type-safe and fluent manner.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface ItemGroupPostRegistrable
    extends PostRegistrable<ItemGroup, ItemGroupPostRegistrable, ItemGroupRegister>
{

}