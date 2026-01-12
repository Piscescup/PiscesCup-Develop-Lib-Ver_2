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
 * <h2>Description</h2>
 *
 * <h2>Usages</h2>
 *
 * @author REN YuanTong
 * @Date 2026-01-10
 * @since 1.0.0
 */
public interface TagKeyPreRegistrable<T> extends PreRegistrable<TagKeyPostRegistrable<T>> {
    TagKeyPreRegistrable<T> add(@NotNull T item);

    default TagKeyPreRegistrable<T> addAll(@NotNull Collection<@NotNull T> items) {
        items.forEach(this::add);
        return this;
    }

    default TagKeyPreRegistrable<T> addAll(@NotNull T[] items) {
        return this.addAll(List.of(items));
    }

    default TagKeyPreRegistrable<T> addAll(Stream<T> items) {
        items.forEach(this::add);
        return this;
    }

    TagKeyPreRegistrable<T> addTag(@NotNull TagKey<T> tag);

    default TagKeyPreRegistrable<T> addTags(@NotNull Collection<TagKey<T>> tags) {
        tags.forEach(this::addTag);
        return this;
    }

    default TagKeyPreRegistrable<T> addTags(@NotNull TagKey<T>[] tags) {
        return this.addTags(List.of(tags));
    }

    TagKeyPreRegistrable<T> add(Function<RegistryWrapper.WrapperLookup, Stream<T>> mappingFunction);

}
