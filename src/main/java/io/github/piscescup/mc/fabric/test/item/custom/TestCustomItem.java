package io.github.piscescup.mc.fabric.test.item.custom;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * <h2>Description</h2>
 *
 * <h2>Usages</h2>
 *
 * @author REN YuanTong
 * @Date 2025-12-27
 * @since
 */
public class TestCustomItem extends Item {
    public TestCustomItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        // super.use(world, user, hand);
        if (!world.isClient()) {
            user.sendMessage(
                stack.getName().copy().append(" used!"),
                false
            );

            user.addStatusEffect(
                new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0)
            );

            if (!user.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }

        return ActionResult.SUCCESS;
    }
}
