package io.github.piscescup.mc.fabric.datagen;

import io.github.piscescup.mc.fabric.utils.constant.MCLanguage;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class DataGenerationProviderConfig {
    private final List<FabricDataGenerator.Pack.RegistryDependentFactory<DataProvider>> registryDependentFactories;

    private DataGenerationProviderConfig() {
        this.registryDependentFactories = new ArrayList<>();
    }

    public static DataGenerationProviderConfig create() {
        return new DataGenerationProviderConfig();
    }

    public DataGenerationProviderConfig addRegistryFactory(DataGenOption option) {
        this.registryDependentFactories.add(option.toRegistryDependentFactory());
        return this;
    }

    public void applyTo(FabricDataGenerator.Pack pack) {
        this.registryDependentFactories.forEach(pack::addProvider);
    }
}
