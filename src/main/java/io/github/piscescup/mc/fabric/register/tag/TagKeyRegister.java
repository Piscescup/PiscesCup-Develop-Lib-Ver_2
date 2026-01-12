package io.github.piscescup.mc.fabric.register.tag;

import io.github.piscescup.mc.fabric.register.Register;
import io.github.piscescup.mc.fabric.utils.CheckUtils.NullCheck;
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
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TagKeyRegister<T>
    extends Register<TagKey<T>, TagKeyPostRegistrable<T>, TagKeyRegister<T>>
    implements TagKeyPreRegistrable<T>, TagKeyPostRegistrable<T>
{
    private final List<T> content;
    private final List<TagKey<T>> tags;
    private final List<Function<RegistryWrapper.WrapperLookup, Stream<T>>> mappings;

    private TagKeyRegister(RegistryKey<? extends Registry<T>> registryRef, Identifier id) {
        super();
        this.id = id;
        this.thing = TagKey.of(registryRef, id);

        this.content = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.mappings = new ArrayList<>();
    }

    @Contract("_, _ -> new")
    public static <T> @NotNull TagKeyPreRegistrable<T> createFor(
        RegistryKey<? extends Registry<T>> registryRef,
        @NotNull Identifier id
    ) {
        NullCheck.requireNonNull(id);
        return new TagKeyRegister<>(registryRef, id);
    }

    public static <T> @NotNull TagKeyPreRegistrable<T> createFor(
        RegistryKey<? extends Registry<T>> registryRef,
        @NotNull String namespace, @NotNull String path
    ) {
        NullCheck.requireNonNull(namespace);
        NullCheck.requireNonNull(path);
        return createFor(registryRef, Identifier.of(namespace, path));
    }

    @Override
    public TagKeyPreRegistrable<T> add(@NotNull T item) {
        NullCheck.requireNonNull(item);
        this.content.add(item);
        return this;
    }

    @Override
    public TagKeyPreRegistrable<T> addAll(@NotNull Collection<T> items) {
        NullCheck.requireNonNull(items);
        NullCheck.requireAllNonNull(items);
        this.content.addAll(items);
        return this;
    }

    @Override
    public TagKeyPreRegistrable<T> addTag(@NotNull TagKey<T> tag) {
        NullCheck.requireNonNull(tag);
        this.tags.add(tag);
        return this;
    }

    @Override
    public TagKeyPreRegistrable<T> addTags(@NotNull Collection<TagKey<T>> tags) {
        NullCheck.requireNonNull(tags);
        NullCheck.requireAllNonNull(tags);
        this.tags.addAll(tags);
        return this;
    }

    @Override
    public TagKeyPreRegistrable<T> add(Function<RegistryWrapper.WrapperLookup, Stream<T>> mappingFunction) {
        NullCheck.requireNonNull(mappingFunction);
        this.mappings.add(mappingFunction);
        return null;
    }

    @Override
    public TagKeyPostRegistrable<T> register() {
        return this;
    }

    public List<T> getContent() {
        return content;
    }

    public List<TagKey<T>> getTags() {
        return tags;
    }

    public List<Function<RegistryWrapper.WrapperLookup, Stream<T>>> getMappings() {
        return mappings;
    }
}
