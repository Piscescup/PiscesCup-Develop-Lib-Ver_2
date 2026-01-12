package io.github.piscescup.mc.fabric.datagen.tag;

import io.github.piscescup.mc.fabric.utils.CheckUtils;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.data.tag.TagProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class TagDataGenProvider<T> extends TagProvider<T> {
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

            });
    }

    protected ProvidedTagBuilder<T, T> builder(TagKey<T> tag) {
        return
    }
}
