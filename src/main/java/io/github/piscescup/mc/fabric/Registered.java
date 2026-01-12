package io.github.piscescup.mc.fabric;

import static io.github.piscescup.mc.fabric.References.*;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface Registered {
    default void register(String modName) {
        MOD_LOGGER.info(
            "Registering {} for {}, by {} version {}",
            this.getClass().getSimpleName(), modName, MOD_NAME, MOD_VERSION
        );
    }
}
