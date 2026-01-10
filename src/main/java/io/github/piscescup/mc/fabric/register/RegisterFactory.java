package io.github.piscescup.mc.fabric.register;

import static io.github.piscescup.mc.fabric.References.MOD_LOGGER;

/**
 * <h2>Description</h2>
 *
 * <h2>Usages</h2>
 *
 * @author REN YuanTong
 * @Date 2025-12-29
 * @since 1.0.0
 */
public final class RegisterFactory {
    private RegisterFactory() {
        MOD_LOGGER.error(
            "Attempted to instantiate {}", RegisterFactory.class.getCanonicalName()
        );
        throw new UnsupportedOperationException();
    }


}
