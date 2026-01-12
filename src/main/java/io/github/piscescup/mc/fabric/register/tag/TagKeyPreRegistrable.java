package io.github.piscescup.mc.fabric.register.tag;

import io.github.piscescup.mc.fabric.register.PreRegistrable;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;


/**
 * Represents the pre-registration stage for building a tag entry set for a {@link TagKey}.
 *
 * <p>This interface collects elements (of type {@code T}) and/or other tags that should be
 * included in a target tag during the data generation or registration phase. It is designed
 * for fluent chaining and participates in a two-phase workflow by extending
 * {@link PreRegistrable} and producing a {@link TagKeyPostRegistrable} via {@link #register()}.
 *
 *
 * @param <T> the element type contained by the tag (e.g., {@code Item}, {@code Block})
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface TagKeyPreRegistrable<T> extends PreRegistrable<TagKeyPostRegistrable<T>> {

    /**
     * Adds a single element to the target tag.
     *
     * @param item the element to add
     * @return this builder instance for fluent chaining
     */
    TagKeyPreRegistrable<T> add(@NotNull T item);

    /**
     * Adds all elements from the given collection to the target tag.
     *
     * <p>This default implementation delegates to {@link #add(Object)} for each element.
     *
     * @param items the elements to add
     * @return this builder instance for fluent chaining
     */
    default TagKeyPreRegistrable<T> addAll(@NotNull Collection<@NotNull T> items) {
        items.forEach(this::add);
        return this;
    }

    /**
     * Adds all elements from the given array to the target tag.
     *
     * <p>This default implementation delegates to {@link #addAll(Collection)}.
     *
     * @param items the elements to add
     * @return this builder instance for fluent chaining
     */
    default TagKeyPreRegistrable<T> addAll(@NotNull T[] items) {
        return this.addAll(List.of(items));
    }

    /**
     * Adds all elements produced by the given stream to the target tag.
     *
     * <p>This default implementation consumes the stream and delegates to {@link #add(Object)}
     * for each element.
     *
     * @param items the stream producing elements to add
     * @return this builder instance for fluent chaining
     */
    default TagKeyPreRegistrable<T> addAll(Stream<T> items) {
        items.forEach(this::add);
        return this;
    }

    /**
     * Adds another tag as an entry to the target tag.
     *
     * <p>This is typically used to include all elements of {@code tag} into the target tag.
     *
     * @param tag the referenced tag to add
     * @return this builder instance for fluent chaining
     */
    TagKeyPreRegistrable<T> addTag(@NotNull TagKey<T> tag);

    /**
     * Adds all referenced tags from the given collection to the target tag.
     *
     * <p>This default implementation delegates to {@link #addTag(TagKey)} for each tag.
     *
     * @param tags the tags to add
     * @return this builder instance for fluent chaining
     */
    default TagKeyPreRegistrable<T> addTags(@NotNull Collection<TagKey<T>> tags) {
        tags.forEach(this::addTag);
        return this;
    }

    /**
     * Adds all referenced tags from the given array to the target tag.
     *
     * <p>This default implementation delegates to {@link #addTags(Collection)}.
     *
     * @param tags the tags to add
     * @return this builder instance for fluent chaining
     */
    default TagKeyPreRegistrable<T> addTags(@NotNull TagKey<T>[] tags) {
        return this.addTags(List.of(tags));
    }

    /**
     * Adds elements produced by a lookup-dependent mapping function.
     *
     * <p>The supplied function will receive a {@link RegistryWrapper.WrapperLookup} at
     * registration/data generation time and should return a {@link Stream} of elements
     * that will be added to the target tag. This allows implementations to resolve
     * registry entries lazily (e.g., by reading from registries available in the
     * current datagen context).
     *
     * <p>Implementations may choose whether to evaluate the function immediately or
     * defer it until {@link #register()}; however, it should be evaluated at most once
     * per registration pass.
     *
     * @param mappingFunction a function that maps a {@link RegistryWrapper.WrapperLookup}
     *                        to a stream of elements to add
     * @return this builder instance for fluent chaining
     */
    TagKeyPreRegistrable<T> add(Function<RegistryWrapper.WrapperLookup, Stream<T>> mappingFunction);
}