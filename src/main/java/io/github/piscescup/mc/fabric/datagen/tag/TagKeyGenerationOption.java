package io.github.piscescup.mc.fabric.datagen.tag;


import io.github.piscescup.mc.fabric.datagen.DataGenOption;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TagKeyGenerationOption<T> implements DataGenOption {
    private final RegistryKey<? extends Registry<T>> registryKey;
    private final TagKeyRegisterList<T> tagKeyRegisters;


    private TagKeyGenerationOption(RegistryKey<? extends Registry<T>> registryKey) {
        this.registryKey = registryKey;
        this.tagKeyRegisters = TagKeysContainer.getTagKeyRegisters(registryKey);
    }

    @Override
    public @NotNull FabricDataGenerator.Pack.RegistryDependentFactory<DataProvider> toRegistryDependentFactory() {
        return (output, registriesFuture) -> new TagDataGenProvider<>(output, this, registriesFuture);
    }

    public RegistryKey<? extends Registry<T>> getRegistryKey() {
        return registryKey;
    }

    public TagKeyRegisterList<T> getTagKeyRegisters() {
        return tagKeyRegisters;
    }
}
