package io.github.piscescup.mc.fabric;

import io.github.piscescup.mc.fabric.datagen.DataGenerationProviderConfig;
import io.github.piscescup.mc.fabric.datagen.tag.TagKeyGenerationOption;
import io.github.piscescup.mc.fabric.utils.constant.MCLanguageOption;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class PCDevelopLibDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		DataGenerationProviderConfig.create()
			.addLanguageFactory(MCLanguageOption.ZH_CN)
			.addLanguageFactory(MCLanguageOption.EN_US)
			.addTagKeyFactory(TagKeyGenerationOption.ITEM_TAGS)
			.applyTo(pack);

	}
}
