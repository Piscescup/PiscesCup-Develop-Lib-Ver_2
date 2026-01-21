package io.github.piscescup.mc.fabric.datagen.tag;

import io.github.piscescup.mc.fabric.datagen.DataGenOption;
import io.github.piscescup.mc.fabric.register.tag.TagKeyRegister;
import io.github.piscescup.mc.fabric.utils.CheckUtils;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.block.Block;
import net.minecraft.data.DataProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.poi.PointOfInterestType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;



/**
 * A {@link DataGenOption} describing tag data generation for a specific {@link Registry}.
 *
 * <p>{@code TagKeyGenerationOption} binds a registry (such as {@link RegistryKeys#ITEM} or
 * {@link RegistryKeys#BLOCK}) to the corresponding {@link TagKeyRegisterList} stored in
 * {@link TagKeysContainer}. It can then be converted into a
 * {@link net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack.RegistryDependentFactory}
 * that produces a {@link DataProvider} (namely {@link TagDataGenProvider}) for Fabric data generation.
 *
 * <p>This option is typically added to a {@link net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack}
 * so that tag JSON files are generated for the selected registry. The collected tag registrations
 * (see {@link TagKeyRegister}) are expected to be populated elsewhere prior to running datagen.
 *
 * <p>Convenience constants are provided for common registries:
 * {@link #ITEM_TAGS}, {@link #BLOCK_TAGS}, {@link #FLUID_TAGS}, and {@link #ENTITY_TYPE_TAGS}.
 *
 * @param <T> the element type of the target registry (e.g., {@link Item}, {@link Block})
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TagKeyGenerationOption<T> implements DataGenOption {

    /**
     * Tag data generation option for {@link RegistryKeys#ITEM}.
     */
    public static final TagKeyGenerationOption<Item> ITEM_TAGS =
        createFor(RegistryKeys.ITEM);

    /**
     * Tag data generation option for {@link RegistryKeys#BLOCK}.
     */
    public static final TagKeyGenerationOption<Block> BLOCK_TAGS =
        createFor(RegistryKeys.BLOCK);

    /**
     * Tag data generation option for {@link RegistryKeys#FLUID}.
     */
    public static final TagKeyGenerationOption<Fluid> FLUID_TAGS =
        createFor(RegistryKeys.FLUID);

    /**
     * Tag data generation option for {@link RegistryKeys#ENTITY_TYPE}.
     */
    public static final TagKeyGenerationOption<EntityType<?>> ENTITY_TYPE_TAGS =
        createFor(RegistryKeys.ENTITY_TYPE);

    public static final TagKeyGenerationOption<PointOfInterestType> POINT_OF_INTEREST_TYPE_TAGS =
        createFor(RegistryKeys.POINT_OF_INTEREST_TYPE);

    private final RegistryKey<? extends Registry<T>> registryKey;
    private final TagKeyRegisterList<T> tagKeyRegisters;

    /**
     * Creates a {@code TagKeyGenerationOption} for the given registry key.
     *
     * @param registryKey the target registry key for which tags should be generated
     * @param <T>         the element type of the target registry
     * @return a new {@code TagKeyGenerationOption} instance
     */
    public static <T> TagKeyGenerationOption<T> createFor(
        RegistryKey<? extends Registry<T>> registryKey
    ) {
        CheckUtils.NullCheck.requireNonNull(registryKey);
        return new TagKeyGenerationOption<>(registryKey);
    }

    /**
     * Constructs a new option bound to the provided registry key, and resolves the
     * corresponding {@link TagKeyRegisterList} from {@link TagKeysContainer}.
     *
     * @param registryKey the target registry key
     */
    private TagKeyGenerationOption(
        RegistryKey<? extends Registry<T>> registryKey
    ) {
        this.registryKey = registryKey;
        this.tagKeyRegisters = TagKeysContainer.getTagKeyRegisters(registryKey);
    }

    /**
     * Converts this option into a registry-dependent provider factory for Fabric datagen.
     *
     * <p>The created provider will generate tag JSON files for {@link #getRegistryKey()}
     * using the registrations contained in {@link #getTagKeyRegisters()}.
     *
     * @return a {@link FabricDataGenerator.Pack.RegistryDependentFactory} that creates
     *         a {@link TagDataGenProvider} for this option
     */
    @Override
    public @NotNull FabricDataGenerator.Pack.RegistryDependentFactory<DataProvider> toRegistryDependentFactory() {
        return (output, registriesFuture) ->
            new TagDataGenProvider<>(output, this, registriesFuture);
    }

    /**
     * Returns the target registry key for this tag generation option.
     *
     * @return the registry key
     */
    public RegistryKey<? extends Registry<T>> getRegistryKey() {
        return registryKey;
    }

    /**
     * Returns the tag registrations associated with this option's registry.
     *
     * <p>The returned list is backed by {@link TagKeysContainer} and contains all
     * {@link TagKeyRegister} instances that were enqueued for this registry.
     *
     * @return the tag register list for this option
     */
    public TagKeyRegisterList<T> getTagKeyRegisters() {
        return tagKeyRegisters;
    }
}
