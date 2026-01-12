package io.github.piscescup.mc.fabric.test.itemgroup;

import io.github.piscescup.mc.fabric.register.itemgroup.ItemGroupRegister;
import io.github.piscescup.mc.fabric.test.item.PCDevLibTestItem;
import io.github.piscescup.mc.fabric.utils.constant.MCLanguageOption;
import net.minecraft.item.ItemGroup;

import static io.github.piscescup.mc.fabric.References.*;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class PCDevLibTestItemGroup {
    public static final ItemGroup TEST_ITEM_GROUP_1 = ItemGroupRegister.createFor(MOD_ID, "test_item_group_1")
        .position(ItemGroup.Row.TOP, 8)
        .icon(PCDevLibTestItem.TEST_ITEM1)
        .defaultAppearance()
        .defaultTexture()
        .addItemEntries(PCDevLibTestItem.PC_DEV_LIB_ITEMS)
        .register()
        .translate(MCLanguageOption.EN_US, "Test Item Group 1")
        .translate(MCLanguageOption.ZH_CN, "测试创造物品栏 1")
        .get();


    public static void register() {
        MOD_LOGGER.info("Registering ItemGroups for {}", MOD_NAME);
    }
}
