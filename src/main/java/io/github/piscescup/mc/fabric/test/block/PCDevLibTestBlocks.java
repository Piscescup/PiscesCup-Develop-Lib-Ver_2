package io.github.piscescup.mc.fabric.test.block;

import io.github.piscescup.mc.fabric.Registered;
import io.github.piscescup.mc.fabric.register.block.BlockRegister;
import io.github.piscescup.mc.fabric.utils.constant.MCLanguageOption;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

import static io.github.piscescup.mc.fabric.References.MOD_ID;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class PCDevLibTestBlocks implements Registered {
    public static final Block TEST_BLOCK = BlockRegister.createFor(MOD_ID, "test_block")
        .settings(AbstractBlock.Settings.create()
            .hardness(1.0f)
            .requiresTool()
        )
        .register()
        .translate(MCLanguageOption.EN_US, "PCDevLib Test Block")
        .translate(MCLanguageOption.ZH_CN, "PCDevLib 测试方块")
        .get();
}
