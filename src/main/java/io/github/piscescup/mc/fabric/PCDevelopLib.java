package io.github.piscescup.mc.fabric;

import io.github.piscescup.mc.fabric.test.item.PCDevLibTestItems;
import io.github.piscescup.mc.fabric.test.itemgroup.PCDevLibTestItemGroups;
import io.github.piscescup.mc.fabric.test.poi.PCDevLibTestPOIs;
import io.github.piscescup.mc.fabric.test.tag.PCDevLibTestItemTags;
import io.github.piscescup.mc.fabric.test.tag.PCDevLibTestPOITags;
import io.github.piscescup.mc.fabric.test.villager.PCDevLibTestVillagers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import static io.github.piscescup.mc.fabric.References.*;


public class PCDevelopLib implements ModInitializer {

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		MOD_LOGGER.info("Hello Fabric world!");
        MOD_LOGGER.info(
            "Hello, {} (ver. {}), based on Minecraft {}",
            MOD_NAME, MOD_VERSION, MC_VERSION
        );
        MOD_LOGGER.info(MOD_FLAG);
        MOD_LOGGER.info(THANKS);

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            RegisterLamp.create()
                .addModule(PCDevLibTestItems::new)
                .addModule(PCDevLibTestItemGroups::new)
                .addModule(PCDevLibTestItemTags::new)
                .addModule(PCDevLibTestPOITags::new)
                .addModule(PCDevLibTestPOIs::new)
                .addModule(PCDevLibTestVillagers::new)
                .registerAll(MOD_NAME);
        }


        MOD_LOGGER.info(MOD_FINISH);
	}
}