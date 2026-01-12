package io.github.piscescup.mc.fabric.register.tag;

import io.github.piscescup.mc.fabric.datagen.tag.TagKeysContainer;
import io.github.piscescup.mc.fabric.register.Register;
import io.github.piscescup.mc.fabric.utils.CheckUtils.NullCheck;
import io.github.piscescup.mc.fabric.utils.constant.MCLanguageOption;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Concrete implementation for building and registering a {@link TagKey} along with its contents
 * for data generation.
 *
 * <p>{@code TagKeyRegister} participates in a two-phase registration workflow:
 * <ul>
 *   <li><b>Pre-registration:</b> collect direct elements ({@link #add(Object)}), referenced tags
 *       ({@link #addTag(TagKey)}), and lookup-dependent mappings ({@link #add(Function)}).</li>
 *   <li><b>Registration:</b> {@link #register()} enqueues this register into a central
 *       {@link TagKeysContainer} for the target registry reference. The actual tag JSON emission
 *       and/or resolution is expected to be performed later by the container/provider.</li>
 * </ul>
 *
 * <p>This class implements both {@link TagKeyPreRegistrable} and {@link TagKeyPostRegistrable},
 * allowing a fluent chain where the same instance transitions from configuration to terminal
 * access after {@link #register()}.
 *
 * <h2>Collected Sources</h2>
 * <ul>
 *   <li>{@link #content}: direct entries to be included in the tag</li>
 *   <li>{@link #tags}: referenced tags whose entries should be included</li>
 *   <li>{@link #mappings}: deferred producers evaluated with a {@link RegistryWrapper.WrapperLookup}</li>
 * </ul>
 *
 * <p>Translations are supported via {@link #translate(MCLanguageOption, String)}. A {@code "#"}
 * prefix is automatically applied to the provided translation value to match the conventional
 * tag-style naming used in UI and documentation.
 *
 * @param <T> the element type contained by the tag (e.g., {@code Item}, {@code Block})
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TagKeyRegister<T>
    extends Register<TagKey<T>, TagKeyPostRegistrable<T>, TagKeyRegister<T>>
    implements TagKeyPreRegistrable<T>, TagKeyPostRegistrable<T>
{
    private static final String TAG_PREFIX = "#";

    private final List<T> content;
    private final List<TagKey<T>> tags;
    private final List<Function<RegistryWrapper.WrapperLookup, Stream<T>>> mappings;
    private final RegistryKey<? extends Registry<T>> registryRef;

    private TagKeyRegister(RegistryKey<? extends Registry<T>> registryRef, Identifier id) {
        super();
        this.id = id;
        this.thing = TagKey.of(registryRef, id);

        this.registryRef = registryRef;
        this.content = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.mappings = new ArrayList<>();
    }

    /**
     * Creates a new pre-registrable builder for a {@link TagKey} under the given registry reference.
     *
     * @param registryRef the registry this tag belongs to
     * @param id          the identifier of the tag
     * @param <T>         the element type contained by the tag
     * @return a new {@link TagKeyPreRegistrable} instance
     */
    @Contract("_, _ -> new")
    public static <T> @NotNull TagKeyPreRegistrable<T> createFor(
        RegistryKey<? extends Registry<T>> registryRef,
        @NotNull Identifier id
    ) {
        NullCheck.requireNonNull(id);
        return new TagKeyRegister<>(registryRef, id);
    }

    /**
     * Creates a new pre-registrable builder for a {@link TagKey} under the given registry reference.
     *
     * @param registryRef the registry this tag belongs to
     * @param namespace   the identifier namespace
     * @param path        the identifier path
     * @param <T>         the element type contained by the tag
     * @return a new {@link TagKeyPreRegistrable} instance
     */
    public static <T> @NotNull TagKeyPreRegistrable<T> createFor(
        RegistryKey<? extends Registry<T>> registryRef,
        @NotNull String namespace, @NotNull String path
    ) {
        NullCheck.requireNonNull(namespace);
        NullCheck.requireNonNull(path);
        return createFor(registryRef, Identifier.of(namespace, path));
    }

    /**
     * Adds a single element to this tag's direct content list.
     *
     * @param item the element to add
     * @return this instance for fluent chaining
     */
    @Override
    public TagKeyPreRegistrable<T> add(@NotNull T item) {
        NullCheck.requireNonNull(item);
        this.content.add(item);
        return this;
    }

    /**
     * Adds a referenced tag to be included by this tag.
     *
     * @param tag the referenced tag
     * @return this instance for fluent chaining
     */
    @Override
    public TagKeyPreRegistrable<T> addTag(@NotNull TagKey<T> tag) {
        NullCheck.requireNonNull(tag);
        this.tags.add(tag);
        return this;
    }

    /**
     * Adds a deferred mapping that produces elements to include in this tag.
     *
     * <p>The mapping is stored and is expected to be evaluated later with a
     * {@link RegistryWrapper.WrapperLookup} (typically during data generation).
     *
     * @param mappingFunction a lookup-dependent producer for tag elements
     * @return this instance for fluent chaining
     */
    @Override
    public TagKeyPreRegistrable<T> add(Function<RegistryWrapper.WrapperLookup, Stream<T>> mappingFunction) {
        NullCheck.requireNonNull(mappingFunction);
        this.mappings.add(mappingFunction);
        return this;
    }

    /**
     * Finalizes this builder and enqueues it into the {@link TagKeysContainer} for later processing.
     *
     * <p>This method does not directly emit data. Instead, it registers this instance into a
     * container associated with {@link #registryRef}, where a tag data provider can later read
     * {@link #getContent()}, {@link #getTags()}, and {@link #getMappings()} to produce the final output.
     *
     * @return this instance as a {@link TagKeyPostRegistrable} for post-registration fluent operations
     */
    @Override
    public TagKeyPostRegistrable<T> register() {
        TagKeysContainer.getTagKeyRegisters(this.registryRef)
            .add(this);
        return this;
    }

    /**
     * Adds a translation entry for this tag, prefixing the value with {@code "#"}.
     *
     * <p>This is a convenience for common tag-style UI labels (e.g., {@code "#ores"}).
     *
     * @param lang  the language option
     * @param value the translation value without the {@code "#"} prefix
     * @return this instance for fluent chaining
     */
    @Override
    public TagKeyPostRegistrable<T> translate(@NotNull MCLanguageOption lang, @NotNull String value) {
        return super.translate(lang, TAG_PREFIX + value);
    }

    /**
     * Returns the direct content elements collected for this tag.
     *
     * @return the direct elements
     */
    public List<T> getContent() {
        return content;
    }

    /**
     * Returns the referenced tags collected for this tag.
     *
     * @return the referenced tags
     */
    public List<TagKey<T>> getTags() {
        return tags;
    }

    /**
     * Returns the deferred lookup-dependent mappings collected for this tag.
     *
     * @return the mapping functions
     */
    public List<Function<RegistryWrapper.WrapperLookup, Stream<T>>> getMappings() {
        return mappings;
    }
}
