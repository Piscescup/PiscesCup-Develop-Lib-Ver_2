package io.github.piscescup.mc.fabric.register.villager;

import com.google.common.collect.ImmutableSet;
import io.github.piscescup.mc.fabric.register.Register;
import io.github.piscescup.mc.fabric.utils.CheckUtils;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * The {@code VillagerRegister} class is used to register Minecraft {@link VillagerProfession}s.
 * * <p>This class implements the full suite of villager registration interfaces, including the
 * multi-step builder defined in {@link VillagerPreRegistrable} and the post-registration
 * logic in {@link VillagerPostRegistrable}. Due to the complexity of villager professions,
 * this register guides the developer through a strict configuration pipeline:
 * Workstation -> Acquirability -> Gathering -> Secondary Sites -> Sounds -> Trades.</p>
 *
 * <h2>Usages</h2>
 * <pre>{@code
 * public static final VillagerProfession TEST_VILLAGER_PROFESSION = VillagerRegister.createFor(MOD_ID, "test_profession")
 *     .heldWorkstation(PCDevLibTestPOIs.TEST_VILLAGER_POI)
 *     .defaultAcquirableWorkstation()
 *     .defaultGatherAt()
 *     .defaultSecondaryJobSites()
 *     .workSound(SoundEvents.ENTITY_VILLAGER_DEATH)
 *     .novice(
 *         new TradeOffers.BuyItemFactory(PCDevLibTestItems.CUSTOM_ITEM, 1, 16, 21, 7),
 *         new TradeOffers.BuyItemFactory(Items.BAMBOO, 5, 16, 3, 2),
 *         new TradeOffers.SellItemFactory(PCDevLibTestItems.TEST_ITEM1, 2, 2, 16, 4)
 *     )
 *     .apprentice(
 *         new TradeOffers.BuyItemFactory(Items.FLINT, 30, 12, 20),
 *         new TradeOffers.SellEnchantedToolFactory(Items.IRON_AXE, 1, 3, 10, 0.2F),
 *         new TradeOffers.SellEnchantedToolFactory(Items.IRON_SHOVEL, 2, 3, 10, 0.2F),
 *         new TradeOffers.SellEnchantedToolFactory(Items.IRON_PICKAXE, 3, 3, 10, 0.2F),
 *         new TradeOffers.SellItemFactory(new ItemStack(Items.DIAMOND_HOE), 4, 1, 3, 10, 0.2F)
 *     )
 *     .journeyman(
 *         new TradeOffers.BuyItemFactory(
 *             new TradedItem(Items.POTION).withComponents(builder -> builder.add(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Potions.WATER))),
 *             2, 1, 1
 *         ),
 *         new TradeOffers.BuyItemFactory(Items.WATER_BUCKET, 1, 2, 1, 2),
 *         new TradeOffers.BuyItemFactory(Items.MILK_BUCKET, 1, 2, 1, 2),
 *         new TradeOffers.BuyItemFactory(Items.FERMENTED_SPIDER_EYE, 1, 2, 1, 3),
 *         new TradeOffers.BuyItemFactory(Items.BAKED_POTATO, 4, 2, 1),
 *         new TradeOffers.BuyItemFactory(Items.HAY_BLOCK, 1, 2, 1)
 *     )
 *     .expert(
 *         new TradeOffers.SellItemFactory(Blocks.RED_WOOL, 1, 1, 16, 5),
 *         new TradeOffers.SellItemFactory(Blocks.BLACK_WOOL, 1, 1, 16, 5),
 *         new TradeOffers.SellItemFactory(Blocks.WHITE_CARPET, 1, 4, 16, 5),
 *         new TradeOffers.SellItemFactory(Blocks.ORANGE_CARPET, 1, 4, 16, 5),
 *         new TradeOffers.SellItemFactory(Blocks.MAGENTA_CARPET, 1, 4, 16, 5),
 *         new TradeOffers.SellItemFactory(Blocks.LIGHT_BLUE_CARPET, 1, 4, 16, 5),
 *         new TradeOffers.SellItemFactory(Blocks.YELLOW_CARPET, 1, 4, 16, 5)
 *     )
 *     .master(
 *         new TradeOffers.BuyItemFactory(Items.STRING, 20, 16, 2),
 *         new TradeOffers.BuyItemFactory(Items.COAL, 10, 16, 2),
 *         new TradeOffers.ProcessItemFactory(Items.COD, 6, 1, Items.COOKED_COD, 6, 16, 1, 0.05F),
 *         new TradeOffers.SellItemFactory(Items.COD_BUCKET, 3, 1, 16, 1)
 *     )
 *     .register()
 *     .translate(MCLanguageOption.EN_US, "Test Villager Profession")
 *     .get();
 * }</pre>
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
