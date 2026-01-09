package io.github.piscescup.mc.fabric.datagen.lang;

import io.github.piscescup.mc.fabric.utils.constant.MCLanguage;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

/**
 * <h2>Description</h2>
 *
 * <h2>Usages</h2>
 *
 * @author REN YuanTong
 * @Date 2025-12-18
 * @since 1.0.0
 */
public class LanguageDataGenProvider
    extends FabricLanguageProvider
{
    public LanguageDataGenProvider(
        FabricDataOutput dataOutput,
        MCLanguage lang,
        CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup
    ) {
        super(dataOutput, lang.getCode(), registryLookup);
    }

    @Override
    public void generateTranslations(
        RegistryWrapper.WrapperLookup wrapperLookup,
        TranslationBuilder translationBuilder
    ) {

    }
}
