package io.github.piscescup.mc.fabric.test.tag;

import io.github.piscescup.mc.fabric.Registered;
import io.github.piscescup.mc.fabric.register.tag.TagKeyRegister;
import io.github.piscescup.mc.fabric.test.poi.PCDevLibTestPOIs;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.poi.PointOfInterestType;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class PCDevLibTestPOITags implements Registered {
    public static final TagKey<PointOfInterestType> JOB = TagKeyRegister.VANILLA_ACQUIRABLE_JOB_SITE
        .addRegistryKey(PCDevLibTestPOIs.TEST_VILLAGER_POI)
        .register()
        .get();
}
