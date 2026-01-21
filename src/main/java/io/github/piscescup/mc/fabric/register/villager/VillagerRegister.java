package io.github.piscescup.mc.fabric.register.villager;

import com.google.common.collect.ImmutableSet;
import io.github.piscescup.mc.fabric.datagen.lang.Translation;
import io.github.piscescup.mc.fabric.register.Register;
import io.github.piscescup.mc.fabric.utils.CheckUtils;
import io.github.piscescup.mc.fabric.utils.constant.MCLanguageOption;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class VillagerRegister
    extends Register<VillagerProfession, VillagerPostRegistrable, VillagerRegister>
    implements VillagerPreRegistrable.VillagerWorkStationStage, VillagerPreRegistrable.VillagerAcquirableWorkstationStage,
                VillagerPreRegistrable.VillagerGatherableStage, VillagerPreRegistrable.VillagerSecondaryJobSitesStage,
                VillagerPreRegistrable.VillagerWorkSoundStage, VillagerPreRegistrable.VillagerTradesStage,
                VillagerPostRegistrable
{
    private Predicate<RegistryEntry<PointOfInterestType>> heldWorkstation = PointOfInterestType.NONE;
    private Predicate<RegistryEntry<PointOfInterestType>> acquirableWorkstation = VillagerProfession.IS_ACQUIRABLE_JOB_SITE;
    private ImmutableSet<Item> gatherableItems = ImmutableSet.of();
    private ImmutableSet<Block> secondaryJobSites = ImmutableSet.of();
    private @Nullable SoundEvent workSound = null;

    private final Map<Integer, List<TradeOffers.Factory>> offers;

    private static Map<Integer, List<TradeOffers.Factory>> createOffersFactory() {
        Map<Integer, List<TradeOffers.Factory>> map = new ConcurrentHashMap<>();
        map.put(LEVEL_NOVICE, new ArrayList<>());
        map.put(LEVEL_APPRENTICE, new ArrayList<>());
        map.put(LEVEL_JOURNEYMAN, new ArrayList<>());
        map.put(LEVEL_EXPERT, new ArrayList<>());
        map.put(LEVEL_MASTER, new ArrayList<>());
        return map;
    }

    public static VillagerPreRegistrable.VillagerWorkStationStage createFor(Identifier id) {
        CheckUtils.NullCheck.requireNonNull(id);
        return new VillagerRegister(id);
    }

    public static VillagerPreRegistrable.VillagerWorkStationStage createFor(String namespace, String path) {
        return createFor(Identifier.of(namespace, path));
    }

    private VillagerRegister(Identifier id) {
        super(RegistryKeys.VILLAGER_PROFESSION, id);
        offers = createOffersFactory();
    }

    @Override
    public VillagerPreRegistrable.VillagerAcquirableWorkstationStage heldWorkstation(Predicate<RegistryEntry<PointOfInterestType>> heldWorkStation) {
        CheckUtils.NullCheck.requireNonNull(heldWorkStation);
        this.heldWorkstation = heldWorkStation;
        return this;
    }

    @Override
    public VillagerPreRegistrable.VillagerGatherableStage acquirableWorkstation(Predicate<RegistryEntry<PointOfInterestType>> acquirableWorkstation) {
        CheckUtils.NullCheck.requireNonNull(acquirableWorkstation);
        this.acquirableWorkstation = acquirableWorkstation;
        return this;
    }

    @Override
    public VillagerPreRegistrable.VillagerSecondaryJobSitesStage gatherAt(ImmutableSet<Item> gatheredItems) {
        CheckUtils.NullCheck.requireAllNonNull(gatheredItems);
        this.gatherableItems = ImmutableSet.copyOf(gatheredItems);
        return this;
    }

    @Override
    public VillagerPreRegistrable.VillagerWorkSoundStage secondaryJobSites(ImmutableSet<Block> secondaryJobSites) {
        CheckUtils.NullCheck.requireAllNonNull(secondaryJobSites);
        this.secondaryJobSites = ImmutableSet.copyOf(secondaryJobSites);
        return this;
    }

    @Override
    public VillagerPreRegistrable.VillagerTradesStage workSound(@Nullable SoundEvent workSound) {
        this.workSound = workSound;
        return this;
    }

    @Override
    public VillagerPreRegistrable.VillagerTradesStage novice(TradeOffers.Factory... noviceOffer) {
        CheckUtils.NullCheck.requireNonNull(noviceOffer);
        offers.get(LEVEL_NOVICE).addAll(Arrays.asList(noviceOffer));
        return this;
    }

    @Override
    public VillagerPreRegistrable.VillagerTradesStage apprentice(TradeOffers.Factory... apprenticeOffer) {
        CheckUtils.NullCheck.requireNonNull(apprenticeOffer);
        offers.get(LEVEL_APPRENTICE).addAll(Arrays.asList(apprenticeOffer));
        return this;
    }

    @Override
    public VillagerPreRegistrable.VillagerTradesStage journeyman(TradeOffers.Factory... journeymanOffer) {
        CheckUtils.NullCheck.requireNonNull(journeymanOffer);
        offers.get(LEVEL_JOURNEYMAN).addAll(Arrays.asList(journeymanOffer));
        return this;
    }

    @Override
    public VillagerPreRegistrable.VillagerTradesStage expert(TradeOffers.Factory... expertOffer) {
        CheckUtils.NullCheck.requireNonNull(expertOffer);
        offers.get(LEVEL_EXPERT).addAll(Arrays.asList(expertOffer));
        return this;
    }

    @Override
    public VillagerPreRegistrable.VillagerTradesStage master(TradeOffers.Factory... masterOffer) {
        CheckUtils.NullCheck.requireNonNull(masterOffer);
        offers.get(LEVEL_MASTER).addAll(Arrays.asList(masterOffer));
        return this;
    }

    @Override
    public VillagerPostRegistrable register() {
        VillagerProfession profession = new VillagerProfession(
            Text.translatable(villagerTranslateKey(this.id)),
            this.heldWorkstation,
            this.acquirableWorkstation,
            this.gatherableItems,
            this.secondaryJobSites,
            this.workSound
        );

        this.thing = Registry.register(Registries.VILLAGER_PROFESSION, this.id, profession);

        buildOffers();

        return this;
    }

    // @Override
    // public VillagerPostRegistrable translate(@NotNull MCLanguageOption lang, @NotNull String value) {
    //     Translation.putTranslations(
    //         lang,
    //         new Translation.TranslationEntry(
    //             villagerTranslateKey(this.id),
    //             value
    //         )
    //     );
    //     return this;
    // }

    private void buildOffers() {
        buildLevelOffers(LEVEL_NOVICE);
        buildLevelOffers(LEVEL_APPRENTICE);
        buildLevelOffers(LEVEL_JOURNEYMAN);
        buildLevelOffers(LEVEL_EXPERT);
        buildLevelOffers(LEVEL_MASTER);
    }

    private void buildLevelOffers(int level) {
        TradeOfferHelper.registerVillagerOffers(
            this.registryKey,
            level,
            factories -> factories.addAll(offers.get(level))
        );
    }

    static Function<Identifier, String> TRANSLATE_KEY =
        id -> "entity." + id.getNamespace() + ".villager." + id.getPath();

    public static String villagerTranslateKey(Identifier key) {
        CheckUtils.NullCheck.requireNonNull(key);
        return TRANSLATE_KEY.apply(key);
    }
}
