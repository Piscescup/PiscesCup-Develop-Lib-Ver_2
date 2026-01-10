package io.github.piscescup.mc.fabric.datagen;

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
     * Get the name of this data generation option.
     * @return The name of this data generation option.
     */
    @NotNull
    String getDataGenOptionName();

}
