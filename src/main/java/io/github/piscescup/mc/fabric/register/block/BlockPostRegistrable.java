package io.github.piscescup.mc.fabric.register.block;

import io.github.piscescup.mc.fabric.register.PostRegistrable;
import net.minecraft.block.Block;

/**
 * <h2>Description</h2>
 *
 * <h2>Usages</h2>
 *
 * @author REN YuanTong
 * @Date 2025-12-29
 * @since
 */
public interface BlockPostRegistrable
    extends PostRegistrable<Block, BlockPostRegistrable, BlockRegister>
{
}
