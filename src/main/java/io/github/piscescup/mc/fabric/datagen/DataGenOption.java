package io.github.piscescup.mc.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataProvider;
import org.jetbrains.annotations.NotNull;

/**
 * <h2>Description</h2>
 *
 * <h2>Usages</h2>
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface DataGenOption {
    /**
     * Converts the current instance into a {@link FabricDataGenerator.Pack.RegistryDependentFactory RegistryDependentFactory} that can be used to
     * generate data providers based on the provided registry.
     *
     * @return a non-null instance of {@link FabricDataGenerator.Pack.RegistryDependentFactory RegistryDependentFactory} for generating data
     *         providers that depend on the game's registries.
     */
    @NotNull
    FabricDataGenerator.Pack.RegistryDependentFactory<DataProvider> toRegistryDependentFactory();

}
