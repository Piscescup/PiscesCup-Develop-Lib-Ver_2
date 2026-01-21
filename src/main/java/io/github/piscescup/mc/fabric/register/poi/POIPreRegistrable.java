package io.github.piscescup.mc.fabric.register.poi;


import com.google.common.collect.ImmutableSet;
import io.github.piscescup.mc.fabric.register.PreRegistrable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.poi.PointOfInterestType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;


/**
 * Represents a pre-registration builder for registering a Minecraft Point of Interest ({@link PointOfInterestType}).
 *
 * <p>This interface provides a configuration layer to define the necessary parameters for a POI,
 * including the associated {@link BlockState}s, the maximum occupancy (ticket count),
 * and the detection radius (search distance).
 *
 * <p>Implementations should return the builder instance to allow fluent chaining
 * during the registration phase.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface POIPreRegistrable extends PreRegistrable<POIPostRegistrable> {

    /**
     * A stage for configuring a Point of Interest (POI) before it is registered.
     * <p>This configuration defines the physical blocks that constitute the POI and
     * the logic governing how entities interact with and search for it.
     */
    interface Config {
        /**
         * Configures the POI with a specific set of block states, ticket count, and search distance.
         *
         * @param blockStates    The set of {@link BlockState}s that define the POI.
         * @param ticketCount    The maximum number of entities (e.g., villagers) that can use this POI concurrently
         * @param searchDistance The radius within which entities will search for this POI
         * @return A configured instance of {@link POIPreRegistrable} ready for registration
         */
        POIPreRegistrable config(@NotNull Set<BlockState> blockStates, int ticketCount, int searchDistance);

        /**
         * Configures the POI using a single block, ticket count, and search distance.
         * * <p>This is a convenience method that includes all possible {@link BlockState}s of the given block.
         *
         * @param block          The {@link Block} defining the POI; must not be null
         * @param ticketCount    The maximum occupancy for this POI
         * @param searchDistance The search radius for this POI
         * @return A configured instance of {@link POIPreRegistrable}
         */
        default POIPreRegistrable config(@NotNull Block block, int ticketCount, int searchDistance) {
            return config(getStatesOfBlock(block), ticketCount, searchDistance);
        }

        /**
         * Configures the POI using a single block and ticket count, with a default search distance of {@code 1}.
         *
         * @param block       The {@link Block} defining the POI; must not be null
         * @param ticketCount The maximum occupancy for this POI
         * @return A configured instance of {@link POIPreRegistrable}
         */
        default POIPreRegistrable config(@NotNull Block block, int ticketCount) {
            return config(getStatesOfBlock(block), ticketCount, 1);
        }

        /**
         * Configures the POI using a single block, with a default ticket count of {@code 1}
         * and a default search distance of {@code 1}.
         *
         * @param block The {@link Block} defining the POI; must not be null
         * @return A configured instance of {@link POIPreRegistrable}
         */
        default POIPreRegistrable config(@NotNull Block block) {
            return config(getStatesOfBlock(block), 1, 1);
        }

        /**
         * Internal helper to retrieve all possible states for a specific block.
         *
         * @param block The block to query
         * @return An immutable set of all valid {@link BlockState}s for the block
         */
        private static Set<BlockState> getStatesOfBlock(Block block) {
            return ImmutableSet.copyOf(block.getStateManager().getStates());
        }
    }
}