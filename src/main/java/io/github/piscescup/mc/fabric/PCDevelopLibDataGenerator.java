package io.github.piscescup.mc.fabric;

import io.github.piscescup.mc.fabric.datagen.DataGenerationProviderConfig;
import io.github.piscescup.mc.fabric.utils.constant.MCLanguageOption;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class PCDevelopLibDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		DataGenerationProviderConfig.create()
			.addRegistryFactory(MCLanguageOption.ZH_CN)
			.addRegistryFactory(MCLanguageOption.EN_US)
			.applyTo(pack);

	}
}
