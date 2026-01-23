package io.github.piscescup.mc.fabric;

import io.github.piscescup.mc.fabric.utils.CheckUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


/**
 * A lightweight registration orchestrator that aggregates multiple
 * {@link Registered} modules and triggers their registration in a single step.
 *
 * <p>{@code RegisterLamp} acts as a simple registry launcher: it collects
 * {@link Supplier suppliers} that lazily provide {@link Registered} instances,
 * and invokes their {@link Registered#register(String)} method when
 * {@link #registerAll(String)} is called.
 *
 * <p>Suppliers are evaluated only at registration time, allowing modules to
 * defer construction until the mod registration phase. This is especially
 * useful when registration depends on environment state, registry availability,
 * or other late-bound conditions.
 *
 * <p>Typical usage:
 * <pre>{@code
 * RegisterLamp.create()
 *     .addModule(ModItems::new)
 *     .addModule(ModBlocks::new)
 *     .registerAll(MOD_ID);
 * }</pre>
 *
 * <p>This class is intentionally final and uses a static factory method
 * to enforce controlled creation and a fluent configuration style.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class RegisterLamp {

    private final List<Supplier<Registered>> registeredModules;

    private RegisterLamp() {
        this.registeredModules = new ArrayList<>();
    }

    /**
     * Creates a new {@code RegisterLamp} instance.
     *
     * @return a new {@code RegisterLamp}
     */
    public static RegisterLamp create() {
        return new RegisterLamp();
    }

    /**
     * Adds a registration module supplier to this launcher.
     *
     * <p>The supplied {@link Registered} instance will not be created immediately;
     * instead, it will be instantiated when {@link #registerAll(String)} is invoked.
     *
     * @param moduleSupplier a supplier that provides a {@link Registered} module
     * @return this {@code RegisterLamp} instance for fluent chaining
     */
    public RegisterLamp addModule(Supplier<Registered> moduleSupplier) {
        this.registeredModules.add(moduleSupplier);
        return this;
    }

    /**
     * Instantiates and registers all added modules using the given mod name.
     *
     * <p>Null suppliers are ignored. Each non-null supplier is evaluated,
     * and the resulting {@link Registered} instance has its
     * {@link Registered#register(String)} method invoked.
     *
     * @param modName the mod identifier or namespace used during registration
     */
    public void registerAll(String modName) {
        String name = CheckUtils.NullCheck.requireNonNullOrElse(modName, "<Empty Mod Name>");

        registeredModules.stream()
            .filter(CheckUtils.NullCheck::nonNull)
            .map(Supplier::get)
            .filter(CheckUtils.NullCheck::nonNull)
            .forEach(registered -> registered.register(name));
    }
}