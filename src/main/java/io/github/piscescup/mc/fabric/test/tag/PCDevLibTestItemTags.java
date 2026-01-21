package io.github.piscescup.mc.fabric.test.tag;

import io.github.piscescup.mc.fabric.Registered;
import io.github.piscescup.mc.fabric.register.tag.TagKeyRegister;
import io.github.piscescup.mc.fabric.utils.constant.MCLanguageOption;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import static io.github.piscescup.mc.fabric.References.*;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class PCDevLibTestItemTags implements Registered {
    public static final TagKey<Item> ORES = TagKeyRegister.createFor(RegistryKeys.ITEM, MOD_ID, "ores")
        .addTag(ItemTags.COAL_ORES)
        .addTag(ItemTags.COPPER_ORES)
        .addTag(ItemTags.DIAMOND_ORES)
        .addTag(ItemTags.EMERALD_ORES)
        .addTag(ItemTags.GOLD_ORES)
        .addTag(ItemTags.IRON_ORES)
        .addTag(ItemTags.LAPIS_ORES)
        .addTag(ItemTags.REDSTONE_ORES)
        .register()
        .translate(MCLanguageOption.ZH_CN, "矿石")
        .translate(MCLanguageOption.EN_US, "Ores")
        .get();



}
