package io.github.piscescup.mc.fabric.datagen;

import io.github.piscescup.mc.fabric.datagen.tag.TagKeyGenerationOption;
import io.github.piscescup.mc.fabric.utils.constant.MCLanguageOption;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Central configuration holder for Fabric data generation providers.
 *
 * <p>This class acts as a lightweight, fluent aggregator for multiple
 * {@link FabricDataGenerator.Pack.RegistryDependentFactory RegistryDependentFactory}
 * instances, which are later applied to a {@link FabricDataGenerator.Pack}
 * in a single step.
 *
 * <p>The configuration itself is immutable from the perspective of external
 * construction: instances can only be created via {@link #create()}, and
 * factories are accumulated through fluent {@code addXxxFactory(...)} calls.
 *
 * <p>Typical usage pattern:
 * <pre>{@code
 * DataGenerationProviderConfig.create()
 *     .addRegisterFactory(registerOption)
 *     .addLanguageFactory(languageOption)
 *     .addTagKeyFactory(tagKeyOption)
 *     .applyTo(pack);
 * }</pre>
 *
 * <p>This design allows different datagen concerns (registries, language,
 * tags, etc.) to be composed declaratively without exposing the underlying
 * Fabric API details to higher-level modules.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class DataGenerationProviderConfig {

    /**
     * Collected registry-dependent provider factories that will be
     * registered to the target data generation pack.
     */
    private final List<FabricDataGenerator.Pack.RegistryDependentFactory<DataProvider>>
        registryDependentFactories;

    /**
     * Private constructor.
     *
     * <p>Instances must be created via {@link #create()} to enforce
     * controlled construction and future extensibility.
     */
    private DataGenerationProviderConfig() {
        this.registryDependentFactories = new ArrayList<>();
    }

    /**
     * Creates a new {@code DataGenerationProviderConfig} instance.
     *
     * <p>Each invocation returns a fresh configuration object with
     * no pre-registered factories.
     *
     * @return a new, empty {@code DataGenerationProviderConfig}
     */
    @Contract(value = " -> new", pure = true)
    public static @NotNull DataGenerationProviderConfig create() {
        return new DataGenerationProviderConfig();
    }

    /**
     * Adds a registry-related data generation factory.
     *
     * <p>This method is typically used for providers that generate
     * registry-backed data, such as item models, block states,
     * loot tables, or recipes.
     *
     * <p>The supplied {@link DataGenOption} is converted into a
     * {@link FabricDataGenerator.Pack.RegistryDependentFactory}
     * and stored internally until {@link #applyTo(FabricDataGenerator.Pack)}
     * is invoked.
     *
     * @param option the data generation option describing the provider
     * @return this configuration instance for fluent chaining
     */
    public DataGenerationProviderConfig addRegisterFactory(
        @NotNull DataGenOption option
    ) {
        this.registryDependentFactories.add(option.toRegistryDependentFactory());
        return this;
    }

    /**
     * Adds a language (translation) data generation factory.
     *
     * <p>This method is intended for registering language providers
     * (e.g. {@code en_us}, {@code zh_cn}) in a declarative and modular way.
     *
     * @param lang the language option describing the translation provider
     * @return this configuration instance for fluent chaining
     */
    public DataGenerationProviderConfig addLanguageFactory(
        @NotNull MCLanguageOption lang
    ) {
        this.registryDependentFactories.add(lang.toRegistryDependentFactory());
        return this;
    }

    /**
     * Adds a tag key data generation factory.
     *
     * <p>This method is used for registering tag providers for
     * registry-based objects such as items, blocks, fluids, or entity types.
     *
     * @param tagKey the tag key generation option
     * @return this configuration instance for fluent chaining
     */
    public DataGenerationProviderConfig addTagKeyFactory(
        @NotNull TagKeyGenerationOption<?> tagKey
    ) {
        this.registryDependentFactories.add(tagKey.toRegistryDependentFactory());
        return this;
    }

    /**
     * Applies all collected provider factories to the given data
     * generation pack.
     *
     * <p>Each stored {@link FabricDataGenerator.Pack.RegistryDependentFactory}
     * is registered by invoking {@link FabricDataGenerator.Pack#addProvider}.
     *
     * <p>This method represents the terminal operation of the configuration
     * lifecycle.
     *
     * @param pack the Fabric data generation pack to apply providers to
     */
    public void applyTo(FabricDataGenerator.@NotNull Pack pack) {
        this.registryDependentFactories.forEach(pack::addProvider);
    }
}