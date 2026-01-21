package io.github.piscescup.mc.fabric.test.villager;

import io.github.piscescup.mc.fabric.Registered;
import io.github.piscescup.mc.fabric.register.villager.VillagerRegister;
import io.github.piscescup.mc.fabric.test.item.PCDevLibTestItems;
import io.github.piscescup.mc.fabric.test.poi.PCDevLibTestPOIs;
import io.github.piscescup.mc.fabric.utils.constant.MCLanguageOption;
import net.minecraft.sound.SoundEvents;
import net.minecraft.village.TradeOffers;
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
            new TradeOffers.BuyItemFactory(PCDevLibTestItems.CUSTOM_ITEM, 1, 16, 21, 7)
        )
        .register()
        .translate(MCLanguageOption.EN_US, "Test Villager Profession")
        .get();


}
