package io.github.piscescup.mc.fabric.test.poi;

import io.github.piscescup.mc.fabric.Registered;
import io.github.piscescup.mc.fabric.register.poi.PointOfInterestRegister;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.poi.PointOfInterestType;

import static io.github.piscescup.mc.fabric.References.MOD_ID;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class PCDevLibTestPOIs implements Registered {
    public static final RegistryKey<PointOfInterestType> TEST_VILLAGER_POI = PointOfInterestRegister.createFor(MOD_ID, "test_villager_poi")
        .config(Blocks.STRIPPED_SPRUCE_LOG)
        .register()
        .getRegistryKey();
}
