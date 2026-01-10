package io.github.piscescup.mc.fabric.datagen.lang;

import io.github.piscescup.mc.fabric.util.CheckUtil;
import io.github.piscescup.mc.fabric.util.MCLanguage;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The language generation provider for language data.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class LanguageGenProvider
    extends FabricLanguageProvider
{
    private final List<Translation.TranslationEntry> translations;

    public LanguageGenProvider(
        FabricDataOutput dataOutput,
        @NotNull MCLanguage lang,
        CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup
    ) {
        super(dataOutput, lang.langCode(), registryLookup);
        this.translations = Translation.getLangTranslations(lang);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        this.translations.forEach(
            entry -> entry.offerToTranslationBuilder(translationBuilder)
        );
    }
}
