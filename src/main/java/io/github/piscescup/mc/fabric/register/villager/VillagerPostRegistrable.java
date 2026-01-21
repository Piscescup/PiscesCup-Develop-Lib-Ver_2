package io.github.piscescup.mc.fabric.register.villager;

import io.github.piscescup.mc.fabric.register.PostRegistrable;
import net.minecraft.village.VillagerProfession;

/**
 * Represents a post-registration handler for a Minecraft {@link VillagerProfession}.
 *
 * <p>This interface defines behavior that is executed <strong>after</strong> a
 * {@link VillagerProfession} has been registered. It is typically used to perform
 * additional setup, such as injecting trades into existing pools, linking the profession
 * to external mod integrations, or performing deferred logic that requires a fully
 * initialized profession instance.
 *
 * <p>Implementations are expected to integrate with {@link VillagerRegister} and
 * participate in the post-registration lifecycle in a type-safe and fluent manner.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface VillagerPostRegistrable
    extends PostRegistrable<VillagerProfession, VillagerPostRegistrable, VillagerRegister>
{

}
