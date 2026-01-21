package io.github.piscescup.mc.fabric.register.poi;

import io.github.piscescup.mc.fabric.register.PostRegistrable;
import net.minecraft.world.poi.PointOfInterestType;

/**
 * Represents a post-registration handler for a Minecraft {@link PointOfInterestType}.
 *
 * <p>This interface defines behavior that is executed <strong>after</strong> a
 * {@link PointOfInterestType} has been registered. It is typically used to perform
 * additional setup, linking, or deferred logic that depends on the completed
 * registration process, such as associating the POI with specific game mechanics.
 *
 * <p>Implementations are expected to integrate with {@link PointOfInterestRegister}
 * and participate in the post-registration lifecycle in a type-safe and fluent manner.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface POIPostRegistrable
    extends PostRegistrable<PointOfInterestType, POIPostRegistrable, PointOfInterestRegister>
{
}