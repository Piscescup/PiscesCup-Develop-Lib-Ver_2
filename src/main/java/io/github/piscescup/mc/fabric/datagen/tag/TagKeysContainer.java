package io.github.piscescup.mc.fabric.datagen.tag;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

import java.util.*;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TagKeysContainer {
    private static final Map<RegistryKey<? extends Registry<?>>, TagKeyRegisterList<?>>  REGISTRY_TO_REGISTER_MAP =
        new LinkedHashMap<>();


    @SuppressWarnings("unchecked")
    public static <T> TagKeyRegisterList<T> getTagKeyRegisters(RegistryKey<? extends Registry<T>> registryRef) {
        return (TagKeyRegisterList<T>) REGISTRY_TO_REGISTER_MAP
            .computeIfAbsent(registryRef, reg -> new TagKeyRegisterList<>());
    }

}
