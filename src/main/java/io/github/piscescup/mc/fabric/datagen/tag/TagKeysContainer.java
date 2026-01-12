package io.github.piscescup.mc.fabric.datagen.tag;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

import java.util.*;

/**
 * Global container that stores and resolves {@link TagKeyRegisterList} instances
 * per Minecraft {@link RegistryKey registry}.
 *
 * <p>This class maintains a mapping from a registry reference
 * (e.g. {@code RegistryKeys.ITEM}, {@code RegistryKeys.BLOCK}) to a
 * {@link TagKeyRegisterList} that collects tag-key registrations targeting that registry.
 *
 * <p>The container is lazily populated: when a registry is requested via
 * {@link #getTagKeyRegisters(RegistryKey)}, the corresponding register list will be
 * created if it does not already exist.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TagKeysContainer {

    /**
     * Mapping from registry keys to their corresponding tag key register list.
     *
     * <p>A {@link LinkedHashMap} is used to preserve insertion order, which may be
     * useful for deterministic iteration during data generation.
     */
    private static final Map<RegistryKey<? extends Registry<?>>, TagKeyRegisterList<?>>
        REGISTRY_TO_REGISTER_MAP = new LinkedHashMap<>();

    /**
     * Returns (and creates if absent) the {@link TagKeyRegisterList} associated with
     * the given registry key.
     *
     * <p>This method provides a per-registry collection point for tag key
     * registrations. The returned list can then be used by higher-level datagen
     * options (e.g. {@code TagKeyGenerationOption}) to build tag providers.
     *
     * @param registryRef the registry key that identifies the target registry
     * @param <T>         the element type of the target registry
     * @return the register list bound to the given registry
     */
    @SuppressWarnings("unchecked")
    public static <T> TagKeyRegisterList<T> getTagKeyRegisters(
        RegistryKey<? extends Registry<T>> registryRef
    ) {
        return (TagKeyRegisterList<T>) REGISTRY_TO_REGISTER_MAP
            .computeIfAbsent(registryRef, reg -> new TagKeyRegisterList<>());
    }
}
