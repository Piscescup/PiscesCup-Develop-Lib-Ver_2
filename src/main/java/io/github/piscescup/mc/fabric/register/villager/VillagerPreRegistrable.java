package io.github.piscescup.mc.fabric.register.villager;

import com.google.common.collect.ImmutableSet;
import io.github.piscescup.mc.fabric.register.PreRegistrable;
import io.github.piscescup.mc.fabric.utils.CheckUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * Represents a multi-step pre-registration builder for creating a Minecraft {@link VillagerProfession}.
 *
 * <p>Creating a villager profession involves several distinct configuration phases, including
 * workstation assignment, gathering logic, and trade offer definitions. This interface
 * uses a nested builder pattern to ensure all required components are defined in a
 * type-safe, logical sequence.</p>
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface VillagerPreRegistrable {

    /**
     * The step for defining the primary workstation.
     * <p>The "held" workstation typically defines the {@link PointOfInterestType}
     * that a villager must be standing at to restock their trades.</p>
     */
    interface VillagerWorkStationStage {
        /**
         * Sets the predicate used to identify the workstation "held" by this profession.
         * @param heldWorkStation A predicate to match the workstation POI
         */
        VillagerAcquirableWorkstationStage heldWorkstation(Predicate<RegistryEntry<PointOfInterestType>> heldWorkStation);

        /**
         * Sets the specific workstation POI key that this profession uses.
         * @param heldWorkStation The registry key of the workstation POI
         */
        default VillagerAcquirableWorkstationStage heldWorkstation(RegistryKey<PointOfInterestType> heldWorkStation) {
            CheckUtils.NullCheck.requireNonNull(heldWorkStation);
            return this.heldWorkstation(entry -> entry.matchesKey(heldWorkStation));
        }
    }

    /**
     * The step for defining which workstations this villager is allowed to acquire.
     */
    interface VillagerAcquirableWorkstationStage {
        /**
         * Sets the predicate for workstations the villager can actively seek out and claim.
         * @param acquirableWorkstation A predicate for claimable POIs
         */
        VillagerGatherableStage acquirableWorkstation(Predicate<RegistryEntry<PointOfInterestType>> acquirableWorkstation);

        /**
         * Sets the specific workstation POI key that the villager can acquire.
         * @param acquirableWorkstation The registry key of the POI
         */
        default VillagerGatherableStage acquirableWorkstation(RegistryKey<PointOfInterestType> acquirableWorkstation) {
            CheckUtils.NullCheck.requireNonNull(acquirableWorkstation);
            return this.acquirableWorkstation(entry -> entry.matchesKey(acquirableWorkstation));
        }

        /**
         * Sets the acquirable workstation to {@link PointOfInterestType#NONE}.
         */
        default VillagerGatherableStage defaultAcquirableWorkstation() {
            return acquirableWorkstation(PointOfInterestType.NONE);
        }
    }

    /**
     * The step for defining items the villager can pick up.
     */
    interface VillagerGatherableStage {
        /**
         * Defines the set of items this villager profession will look for and pick up from the ground.
         * @param gatheredItems An immutable set of items
         */
        VillagerSecondaryJobSitesStage gatherAt(ImmutableSet<Item> gatheredItems);

        /**
         * Configures the villager to not gather any specific items by default.
         */
        default VillagerSecondaryJobSitesStage defaultGatherAt() {
            return gatherAt(ImmutableSet.of());
        }
    }

    /**
     * The step for defining secondary blocks used by the profession.
     */
    interface VillagerSecondaryJobSitesStage {
        /**
         * Defines blocks that are related to the profession but are not the primary workstation
         * (e.g., composters for farmers).
         * @param secondaryJobSites An immutable set of blocks
         */
        VillagerWorkSoundStage secondaryJobSites(ImmutableSet<Block> secondaryJobSites);

        /**
         * Configures no secondary job sites for this profession.
         */
        default VillagerWorkSoundStage defaultSecondaryJobSites() {
            return secondaryJobSites(ImmutableSet.of());
        }
    }

    /**
     * The step for defining the profession's ambient work sound.
     */
    interface VillagerWorkSoundStage {
        /**
         * Sets the sound event played when the villager is performing professional tasks.
         * @param workSound The sound to play, or {@code null} for silence
         */
        VillagerTradesStage workSound(@Nullable SoundEvent workSound);

        /**
         * Configures the profession to have no specific work sound.
         */
        default VillagerTradesStage defaultWorkSound() {
            return workSound(null);
        }
    }

    /**
     * The step for defining trade offers across different experience levels.
     * <p>This interface allows for the registration of {@link TradeOffers.Factory}
     * instances for each tier, from Novice to Master.</p>
     */
    interface VillagerTradesStage extends PreRegistrable<VillagerPostRegistrable> {
        public static int LEVEL_NOVICE = 1;
        public static int LEVEL_APPRENTICE = 2;
        public static int LEVEL_JOURNEYMAN = 3;
        public static int LEVEL_EXPERT = 4;
        public static int LEVEL_MASTER = 5;


        /**
         * Adds trade offers for the Novice level (Level 1).
         * @param noviceOffer Factories for novice trades
         */
        VillagerTradesStage novice(TradeOffers.Factory... noviceOffer);

        /**
         * Adds trade offers for the Apprentice level (Level 2).
         * @param apprenticeOfferOffer Factories for apprentice trades
         */
        VillagerTradesStage apprentice(TradeOffers.Factory... apprenticeOfferOffer);

        /**
         * Adds trade offers for the Journeyman level (Level 3).
         * @param journeymanOffer Factories for journeyman trades
         */
        VillagerTradesStage journeyman(TradeOffers.Factory... journeymanOffer);

        /**
         * Adds trade offers for the Expert level (Level 4).
         * @param expertOffer Factories for expert trades
         */
        VillagerTradesStage expert(TradeOffers.Factory... expertOffer);

        /**
         * Adds trade offers for the Master level (Level 5).
         * @param masterOffer Factories for master trades
         */
        VillagerTradesStage master(TradeOffers.Factory... masterOffer);
    }
}