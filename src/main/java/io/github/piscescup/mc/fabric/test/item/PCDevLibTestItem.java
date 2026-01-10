package io.github.piscescup.mc.fabric.test.item;

import io.github.piscescup.mc.fabric.register.item.ItemRegister;
import io.github.piscescup.mc.fabric.test.item.custom.TestCustomItem;
import io.github.piscescup.mc.fabric.util.MCLanguage;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;

import java.util.ArrayList;
import java.util.List;

import static io.github.piscescup.mc.fabric.References.*;

/**
 * Test Item for PiscesCup Develop Lib.
 *
 * @author REN YuanTong
 * @Date 2025-12-27
 * @since 1.0.0
 */
public final class PCDevLibTestItem {
    public static final List<Item> PC_DEV_LIB_ITEMS = new ArrayList<>();

    public static final Item TEST_ITEM1 = ItemRegister.createForItem(MOD_ID, "test_item1")
        .setting(new Item.Settings()
            .maxCount(1)
            .fireproof()
        )
        .register()
        .translate(MCLanguage.EN_US, "PCDevLib Test Item 1")
        .translate(MCLanguage.ZH_CN, "PCDevLib 测试物品 1")
        .collectsTo(PC_DEV_LIB_ITEMS)
        .get();

    public static final Item CUSTOM_ITEM = ItemRegister.createForItem(MOD_ID, "custom_item")
        .setting(new Item.Settings()
            .maxCount(16)
        )
        .factory(TestCustomItem::new)
        .register()
        .translate(MCLanguage.EN_US, "PCDevLib Custom Item")
        .translate(MCLanguage.ZH_CN, "PCDevLib 自定义物品")
        .collectsTo(PC_DEV_LIB_ITEMS)
        .get();

    public static void register() {
        MOD_LOGGER.info(
            "Registering Items for {}", MOD_NAME
        );
    }
}
