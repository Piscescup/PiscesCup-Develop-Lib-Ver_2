package io.github.piscescup.mc.fabric.register;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.NonNull;

import static io.github.piscescup.mc.fabric.References.MOD_LOGGER;

/**
 * Abstract base for a registration wrapper that participates in a two-phase
 * registration workflow: a pre-registration step that produces a fluent
 * post-registration instance, and post-registration fluent configuration
 * with terminal access to the registered object and its registry key.
 * <p>
 * Implementations typically prepare or construct the object to register
 * during {@link #register()} and then allow additional configuration (for
 * example localization or metadata) before the final object is retrieved via
 * the terminal operations, such as {@link #get()}, {@link #getRegister()}, {@link #getRegistryKey()}.
 *
 * @param <T>    The type of the object being registered (e.g. item, block)
 * @param <POST> The fluent {@code PostRegistrable} type returned by {@link #register()}
 *               and used for chaining post-registration operations
 * @param <R>    The concrete {@code Register} subtype (self type) used for {@link #getRegister()}
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public abstract
class Register<T, POST extends PostRegistrable<T, POST, R>, R extends Register<T, POST, R>>
    implements PreRegistrable<POST>, PostRegistrable<T, POST, R>
{
    protected Identifier id;
    protected T thing;
    protected RegistryKey<T> registryKey;

    protected Register() {}

    protected Register(RegistryKey<? extends Registry<T>> registry , Identifier id) {
        this.id = id;
        this.registryKey = RegistryKey.of(registry, id);
    }

    @Override
    public abstract POST register();

    /**
     * Returns the thing to be registered.
     * @return The thing to be registered
     * @throws IllegalStateException if the thing to be registered is null
     */
    @Override
    public @NonNull T get() {
        if (thing == null) {
            MOD_LOGGER.error(
                "The thing to be registered is null."
            );
            throw new IllegalStateException("The thing to be registered must not be null.");
        }
        return thing;
    }

    /**
     * Returns the registry key associated with the registered object.
     * @return The registry key of the registered object.
     * @see RegistryKey
     * @throws NullPointerException if the registryKey is null
     */
    @Override
    public @NonNull RegistryKey<T> getRegistryKey() {
        if (registryKey == null) {
            MOD_LOGGER.error(
                "The registry key is null."
            );
            throw new IllegalStateException("The registry key must not be null.");
        }
        return registryKey;
    }

    /**
     * Returns this register instance as its concrete self type.
     * <p>
     * This method relies on the CRTP pattern and is safe
     * as long as subclasses bind {@code R} to themselves.
     */
    @Override
    @SuppressWarnings("unchecked")
    public @NonNull R getRegister() {
        return (R) this;
    }
}
