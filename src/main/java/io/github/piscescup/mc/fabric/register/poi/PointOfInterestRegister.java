package io.github.piscescup.mc.fabric.register.poi;

import io.github.piscescup.mc.fabric.register.Register;
import io.github.piscescup.mc.fabric.utils.CheckUtils;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;


/**
 * The {@code PointOfInterestRegister} class is used to register Minecraft {@link PointOfInterestType}s.
 * * <p>This class implements {@link POIPreRegistrable.Config}, {@link POIPreRegistrable}, and
 * {@link POIPostRegistrable} interfaces, providing a comprehensive fluent API for the entire
 * registration lifecycle. It allows developers to define the associated {@link BlockState}s,
 * occupancy limits (ticket count), and search logic before the POI is finalized in the registry.
 * </p>
 *
 * <h2>Usages</h2>
 * <pre>{@code
 * // Registering a POI using a single block with custom ticket count and search distance
 * public static final RegistryKey<PointOfInterestType> TEST_VILLAGER_POI = PointOfInterestRegister.createFor(MOD_ID, "test_villager_poi")
 *         .config(Blocks.STRIPPED_SPRUCE_LOG)
 *         .register()
 *         .getRegistryKey();
 * }</pre>
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class PointOfInterestRegister
    extends Register<PointOfInterestType, POIPostRegistrable, PointOfInterestRegister>
    implements POIPreRegistrable.Config, POIPreRegistrable, POIPostRegistrable
{
    /**
     * The set of block states that are recognized as this Point of Interest.
     */
    private Set<BlockState> blockStates;

    /**
     * The maximum number of tickets (typically slots for entities like villagers)
     * available for this POI.
     */
    private int ticketCount;

    /**
     * The radius or distance within which the game logic will search for this POI.
     */
    private int searchDistance;

    /**
     * Constructs a new register instance for a Point of Interest.
     * * @param id The unique identifier for this POI; must not be null
     */
    public PointOfInterestRegister(@NotNull Identifier id) {
        CheckUtils.NullCheck.requireNonNull(id);
        super(RegistryKeys.POINT_OF_INTEREST_TYPE, id);
    }

    /**
     * Entry point for creating a POI registration builder using an {@link Identifier}.
     * * @param id The identifier for the POI
     * @return A configuration builder instance
     */
    @Contract("_ -> new")
    public static @NotNull POIPreRegistrable.Config createFor(Identifier id) {
        return new PointOfInterestRegister(id);
    }

    /**
     * Entry point for creating a POI registration builder using a namespace and path.
     * * @param namespace The mod ID or namespace
     * @param path The unique path for the POI
     * @return A configuration builder instance
     */
    public static @NotNull POIPreRegistrable.Config createFor(String namespace, String path) {
        return new PointOfInterestRegister(Identifier.of(namespace, path));
    }

    /**
     * Configures the core parameters of the POI.
     * * @param blockStates The set of blocks associated with this POI; must not be null
     * @param ticketCount The maximum occupancy
     * @param searchDistance The search radius
     * @return The builder instance for registration
     * @throws NullPointerException if blockStates is null
     */
    @Override
    public POIPreRegistrable config(@NotNull Set<BlockState> blockStates, int ticketCount, int searchDistance) {
        CheckUtils.NullCheck.requireAllNonNull(blockStates);
        this.blockStates = blockStates;
        this.ticketCount = ticketCount;
        this.searchDistance = searchDistance;
        return this;
    }

    /**
     * Finalizes the configuration and registers the {@link PointOfInterestType} into
     * the Minecraft registry.
     * * @return The register instance as a post-registrable handler
     */
    @Override
    public POIPostRegistrable register() {
        // Logic to instantiate and register the POI
        this.thing = PointOfInterestHelper.register(this.id, this.ticketCount, this.searchDistance, this.blockStates);
        return this;
    }
}