package io.github.piscescup.mc.fabric.datagen.tag;

import io.github.piscescup.mc.fabric.utils.CheckUtils;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.impl.datagen.FabricTagBuilder;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.data.tag.TagProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;

import java.awt.desktop.PrintFilesEvent;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TagDataGenProvider<T> extends FabricTagProvider<T> {
    private final TagKeyRegisterList<T> tagKeyRegisters;

    public TagDataGenProvider(
        FabricDataOutput output,
        TagKeyGenerationOption<T> option,
        CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture
    ) {
        super(output, option.getRegistryKey(), registriesFuture);
        this.tagKeyRegisters = option.getTagKeyRegisters();
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        tagKeyRegisters.forEach(
            register -> {
                TagKey<T> tag = register.get();
                List<T> content = register.getContent();
                List<TagKey<T>> tags = register.getTags();
                List<Function<RegistryWrapper.WrapperLookup, Stream<T>>> functions = register.getMappings();

                Stream<T> stream = functions.stream()
                    .filter(CheckUtils.NullCheck::nonNull)
                    .flatMap(func -> func.apply(wrapperLookup));

                ProvidedTagBuilder<RegistryKey<T>, T> builder = this.builder(tag);
                tags.forEach(
                    builder::addOptionalTag
                );

                Stream.concat(stream, content.stream())
                    .map(this::reverseLookup)
                    .forEach(builder::addOptional);
            });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected RegistryKey<T> reverseLookup(T element) {
        Registry registry = Registries.REGISTRIES.get((RegistryKey) registryRef);

        if (registry != null) {
            Optional<RegistryEntry<T>> key = registry.getKey(element);

            if (key.isPresent()) {
                return (RegistryKey<T>) key.get();
            }
        }

        throw new UnsupportedOperationException("Adding objects is not supported by " + getClass());
    }

}
