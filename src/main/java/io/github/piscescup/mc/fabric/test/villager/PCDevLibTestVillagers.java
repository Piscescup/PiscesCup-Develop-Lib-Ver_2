package io.github.piscescup.mc.fabric.test.villager;

import io.github.piscescup.mc.fabric.Registered;
import io.github.piscescup.mc.fabric.register.villager.VillagerRegister;
import io.github.piscescup.mc.fabric.test.item.PCDevLibTestItems;
import io.github.piscescup.mc.fabric.test.poi.PCDevLibTestPOIs;
import io.github.piscescup.mc.fabric.utils.constant.MCLanguageOption;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundEvents;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;

import static io.github.piscescup.mc.fabric.References.MOD_ID;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class PCDevLibTestVillagers implements Registered {
    public static final VillagerProfession TEST_VILLAGER_PROFESSION = VillagerRegister.createFor(MOD_ID, "test_profession")
        .heldWorkstation(PCDevLibTestPOIs.TEST_VILLAGER_POI)
        .defaultAcquirableWorkstation()
        .defaultGatherAt()
        .defaultSecondaryJobSites()
        .workSound(SoundEvents.ENTITY_VILLAGER_DEATH)
        .novice(
            new TradeOffers.BuyItemFactory(PCDevLibTestItems.CUSTOM_ITEM, 1, 16, 21, 7),
            new TradeOffers.BuyItemFactory(Items.BAMBOO, 5, 16, 3, 2),
            new TradeOffers.SellItemFactory(PCDevLibTestItems.TEST_ITEM1, 2, 2, 16, 4)
        )
        .apprentice(
            new TradeOffers.BuyItemFactory(Items.FLINT, 30, 12, 20),
            new TradeOffers.SellEnchantedToolFactory(Items.IRON_AXE, 1, 3, 10, 0.2F),
            new TradeOffers.SellEnchantedToolFactory(Items.IRON_SHOVEL, 2, 3, 10, 0.2F),
            new TradeOffers.SellEnchantedToolFactory(Items.IRON_PICKAXE, 3, 3, 10, 0.2F),
            new TradeOffers.SellItemFactory(new ItemStack(Items.DIAMOND_HOE), 4, 1, 3, 10, 0.2F)
        )
        .journeyman(
            new TradeOffers.BuyItemFactory(
                new TradedItem(Items.POTION).withComponents(builder -> builder.add(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Potions.WATER))),
                2, 1, 1
            ),
            new TradeOffers.BuyItemFactory(Items.WATER_BUCKET, 1, 2, 1, 2),
            new TradeOffers.BuyItemFactory(Items.MILK_BUCKET, 1, 2, 1, 2),
            new TradeOffers.BuyItemFactory(Items.FERMENTED_SPIDER_EYE, 1, 2, 1, 3),
            new TradeOffers.BuyItemFactory(Items.BAKED_POTATO, 4, 2, 1),
            new TradeOffers.BuyItemFactory(Items.HAY_BLOCK, 1, 2, 1)
        )
        .expert(
            new TradeOffers.SellItemFactory(Blocks.RED_WOOL, 1, 1, 16, 5),
            new TradeOffers.SellItemFactory(Blocks.BLACK_WOOL, 1, 1, 16, 5),
            new TradeOffers.SellItemFactory(Blocks.WHITE_CARPET, 1, 4, 16, 5),
            new TradeOffers.SellItemFactory(Blocks.ORANGE_CARPET, 1, 4, 16, 5),
            new TradeOffers.SellItemFactory(Blocks.MAGENTA_CARPET, 1, 4, 16, 5),
            new TradeOffers.SellItemFactory(Blocks.LIGHT_BLUE_CARPET, 1, 4, 16, 5),
            new TradeOffers.SellItemFactory(Blocks.YELLOW_CARPET, 1, 4, 16, 5)
        )
        .master(
            new TradeOffers.BuyItemFactory(Items.STRING, 20, 16, 2),
            new TradeOffers.BuyItemFactory(Items.COAL, 10, 16, 2),
            new TradeOffers.ProcessItemFactory(Items.COD, 6, 1, Items.COOKED_COD, 6, 16, 1, 0.05F),
            new TradeOffers.SellItemFactory(Items.COD_BUCKET, 3, 1, 16, 1)
        )
        .register()
        .translate(MCLanguageOption.EN_US, "Test Villager Profession")
        .get();


}
