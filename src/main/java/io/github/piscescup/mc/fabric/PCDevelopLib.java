package io.github.piscescup.mc.fabric;

import io.github.piscescup.mc.fabric.test.item.PCDevLibTestItem;
import io.github.piscescup.mc.fabric.test.itemgroup.PCDevLibTestItemGroups;
import io.github.piscescup.mc.fabric.test.tag.PCDevLibTestItemTags;
import net.fabricmc.api.ModInitializer;

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

        RegisterLamp.create()
                .addModule(PCDevLibTestItem::new)
                .addModule(PCDevLibTestItemGroups::new)
                .addModule(PCDevLibTestItemTags::new)
                .registerAll(MOD_NAME);

        MOD_LOGGER.info(MOD_FINISH);
	}
}