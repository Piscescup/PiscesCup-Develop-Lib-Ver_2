package io.github.piscescup.mc.fabric.register.block;

import com.mojang.datafixers.types.Func;
import io.github.piscescup.mc.fabric.register.PreRegistrable;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <h2>Description</h2>
 *
 * <h2>Usages</h2>
 *
 * @author REN YuanTong
 * @Date 2025-12-29
 * @since 1.0.0
 */
public interface BlockPreRegistrable extends PreRegistrable<BlockPostRegistrable> {

    BlockPreRegistrable settings(@NotNull AbstractBlock.Settings settings);

    BlockPreRegistrable factory(@NotNull Function<AbstractBlock.Settings, Block> factory);


}
