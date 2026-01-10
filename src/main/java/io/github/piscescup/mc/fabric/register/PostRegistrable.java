package io.github.piscescup.mc.fabric.register;

import io.github.piscescup.mc.fabric.datagen.lang.LanguageDataGenProvider;
import io.github.piscescup.mc.fabric.datagen.lang.Translation;
import io.github.piscescup.mc.fabric.utils.constant.MCLanguage;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Represents a registrable object that supports subsequent configuration and
 * fluent (chained) operations during the registration process.
 * <p>
 * Implementations typically provide post-build operations such as localization
 * or metadata adjustments and expose terminal operations to obtain the final
 * registered object or its registry key and owning register.
 *
 * <h2>Terminal operation</h2>
 * <p>
 *     The terminal operations in this interface are used to obtain the final
 *     registered object, its registry key, and the register responsible for the
 *     registration process.
 * </p>
 *
 * @param <T>   The type of the object being registered (e.g. item, block)
 * @param <POST> The fluent type returned by chaining methods (usually the implementation type)
 * @param <R>   The Register type responsible for performing the actual registration
 *
 * @author REN Yuantong
 * @since 1.0.0
 * @see PreRegistrable
 * @see Register
 */
public interface PostRegistrable<
    T,
    POST extends PostRegistrable<T, POST, R>,
    R extends Register<T, POST, R>
> {

    /**
     * Add or set a localized text entry for this registrable object (such as a display name
     * or description) for the specified language, and return the fluent instance for chaining.
     *
     * @param lang  Target language enumeration to set the localization for
     * @param value The localized string value for the target language
     * @return the Fluent {@link PostRegistrable} instance for chaining (typically {@code this})
     * @see MCLanguage
     * @see LanguageDataGenProvider
     * @throws NullPointerException if {@code lang} or {@code value} is null
     */
    @SuppressWarnings("unchecked")
    default POST translate(@NotNull MCLanguage lang, @NotNull String value) {
        Translation.putTranslation(this.get(), lang, value);
        return (POST) this;
    }

    @SuppressWarnings("unchecked")
    default POST collectsTo(Collection<T> collection) {
        collection.add(this.get());
        return (POST) this;
    }


    // Terminal Operation.
    /**
     * <p>
     *     Terminal operation.
     * </p>
     * <p>
     *     Returns the thing, such as {@link net.minecraft.item.Item Item}, {@link net.minecraft.block.Block Block}.
     * </p>
     * @return The thing to be registered.
     */
    @NotNull T get();

    /**
     * <p>
     *     Terminal operation.
     * </p>
     * <p>
     *     Returns the registry key of the thing to be registered.
     * </p>
     * @return The registry key of the thing to be registered.
     * @see RegistryKey
     */
    @NotNull RegistryKey<T> getRegistryKey();

    /**
     * <p>
     *     Terminal operation.
     * </p>
     * <p>
     *     Returns the sub-register (type: {@link R}) that performs the registration.
     * </p>
     * @return The register that performs the registration.
     * @see Register
     */
    @NotNull R getRegister();



}
