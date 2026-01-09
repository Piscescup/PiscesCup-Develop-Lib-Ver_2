package io.github.piscescup.mc.fabric.datagen.lang;

import io.github.piscescup.mc.fabric.utils.constant.MCLanguage;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.stat.StatType;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static io.github.piscescup.mc.fabric.References.MOD_LOGGER;

/**
 * <h2>Description</h2>
 *
 * <h2>Usages</h2>
 *
 * @author REN YuanTong
 * @Date 2025-12-18
 * @since 1.0.0
 */
public class Translation {
    private static final Map<MCLanguage, Queue<TranslationEntry>> TRANSLATIONS =
        new ConcurrentHashMap<>();

    public static TranslationEntry putTranslation(
        MCLanguage language,
        Object thing, String translation
    ) {
        Objects.requireNonNull(language);

        TranslationEntry entry = new TranslationEntry(thing, translation);
        TRANSLATIONS
            .computeIfAbsent(language, lang -> new ConcurrentLinkedQueue<>())
            .add(entry);
        return entry;
    }

    public static
    record TranslationEntry(Object thing, String translation) {
        public void offerToTranslationBuilder(FabricLanguageProvider.TranslationBuilder builder) {
            if ( thing instanceof Item item)
                builder.add(item, translation);
            else if ( thing instanceof Block block)
                builder.add(block, translation);
            else if ( thing instanceof EntityType<?> entityType)
                builder.add(entityType, translation);
            else if ( thing instanceof ItemGroup itemGroup){
                TextContent content = itemGroup.getDisplayName().getContent();

                if ( content instanceof TranslatableTextContent translatableTextContent) {
                    builder.add(translatableTextContent.getKey(), translation);
                } else {
                    throw new UnsupportedOperationException(
                        "Cannot add language entry for ItemGroup (%s) as the display name is not translatable."
                            .formatted(itemGroup.getDisplayName().getString())
                    );
                }
            }
            else if ( thing instanceof StatType<?> statType)
                builder.add(statType, translation);
            else if ( thing instanceof StatusEffect statusEffect)
                builder.add(statusEffect, translation);
            else if ( thing instanceof Identifier id )
                builder.add(id, translation);
            else if ( thing instanceof TagKey<?> tagKey )
                builder.add(tagKey, translation);
            else if ( thing instanceof Path existingLanguageFile)
                try {
                    builder.add(existingLanguageFile);
                } catch (IOException e) {
                    MOD_LOGGER.info(
                        "Error while adding existing language file: {}",
                        existingLanguageFile.getFileName()
                    );
                }
            else if ( thing instanceof String key )
                builder.add(key, translation);
            else if ( thing instanceof VillagerProfession profession) {
                TextContent professionName = profession.id().getContent();
                if (professionName instanceof TranslatableTextContent translatableName) {
                    builder.add(translatableName.getKey(), translation);
                } else {
                    MOD_LOGGER.info(
                        "The villager profession {} can not be translated." ,
                        professionName
                    );
                    throw new UnsupportedOperationException(
                        String.format(
                            "The villager profession %s can not be translated." ,
                            professionName
                        )
                    );
                }
            }
        }
    }
}
