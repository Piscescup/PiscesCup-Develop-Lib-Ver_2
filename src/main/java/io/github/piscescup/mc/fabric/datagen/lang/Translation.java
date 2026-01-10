package io.github.piscescup.mc.fabric.datagen.lang;

import io.github.piscescup.mc.fabric.utils.constant.MCLanguage;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.StatType;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static io.github.piscescup.mc.fabric.References.MOD_LOGGER;
import static io.github.piscescup.mc.fabric.utils.CheckUtils.NullCheck;

/**
 * A Translation class is a container of all {@link TranslationEntry} for different languages.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class Translation {
    private static final Map<MCLanguage, Queue<TranslationEntry>> TRANSLATION_MAP = new ConcurrentHashMap<>();

    private Translation() {}

    /**
     * Put a translation entry:{ {@code thing} -> {@code translation}} in the given language into the {@code Translation}.
     * @param thing The thing to be translated.
     * @param lang The language of the translation.
     * @param translation The translation string.
     * @see MCLanguage
     * @throws NullPointerException if {@code thing} or {@code translation} is null.
     */
    public static void putTranslation(Object thing, MCLanguage lang, String translation) {
        NullCheck.requireNonNull(thing, "Thing to be translated must not be null");
        NullCheck.requireNonNull(translation, "Translation must not be null");

        TRANSLATION_MAP
            .computeIfAbsent(lang, mcLang -> new ConcurrentLinkedQueue<>())
            .add(new TranslationEntry(thing, translation));
    }

    /**
     * Put a {@code TranslationEntry} into the {@code Translation}.
     * @param thing The thing to be translated.
     * @param entry The translation entry.
     * @throws NullPointerException if {@code thing} or {@code entry} is null.
     * @see MCLanguage
     */
    public static void putTranslation(MCLanguage thing, TranslationEntry entry) {
        NullCheck.requireNonNull(thing);
        NullCheck.requireNonNull(entry);

        TRANSLATION_MAP
            .computeIfAbsent(thing, mcLang -> new ConcurrentLinkedQueue<>())
            .add(entry);
    }

    /**
     * Put a list of {@code TranslationEntry} into the {@code Translation}.
     * @param lang The language of the translation.
     * @param entries The translation entries.
     * @throws NullPointerException if {@code lang} or {@code entries} is null.
     * @see MCLanguage
     */
    public static void putTranslations(MCLanguage lang, List<TranslationEntry> entries) {
        NullCheck.requireNonNull(lang);
        NullCheck.requireAllNonNull(entries);

        TRANSLATION_MAP
            .computeIfAbsent(lang, mcLang -> new ConcurrentLinkedQueue<>())
            .addAll(entries);
    }

    /**
     * Put some {@code TranslationEntry} into the {@code Translation}.
     * @param lang The language of the translation.
     * @param entries The translation entries.
     * @throws NullPointerException if {@code lang} or {@code entries} is null.
     * @see MCLanguage
     */
    public static void putTranslations(MCLanguage lang, TranslationEntry... entries) {
        NullCheck.requireNonNull(lang);
        NullCheck.requireAllNonNull(entries);

        TRANSLATION_MAP
            .computeIfAbsent(lang, mcLang -> new ConcurrentLinkedQueue<>())
            .addAll(Arrays.asList(entries));
    }

    /**
     * Get all translation entries for the given language.
     * @param lang The language.
     * @return All translation entries for the given language.
     */
    public static List<TranslationEntry> getLangTranslations(MCLanguage lang) {
        NullCheck.requireNonNull(lang);
        Queue<TranslationEntry> queue = TRANSLATION_MAP.get(lang);
        return queue == null ? Collections.emptyList() : List.copyOf(queue);
    }

    /**
     * A translation entry is a key-value pair of an item(such as {@code Item}, {@code Block} and so on) and its translation in specific language.
     * @param thing The thing to be translated.
     * @param translation The translation string.
     */
    public static
    record TranslationEntry(Object thing, String translation) {
        /**
         * Offer this translation entry to the given {@code TranslationBuilder}.
         * @param builder The translation builder.
         * @see FabricLanguageProvider.TranslationBuilder
         */
        public void offerToTranslationBuilder(FabricLanguageProvider.TranslationBuilder builder) {
            if (thing instanceof Item item) {
                builder.add(item, translation);
            } else if (thing instanceof String key) {
                builder.add(key, translation);
            } else if (thing instanceof Block block) {
                builder.add(block, translation);
            } else if (thing instanceof ItemGroup itemGroup) {
                TextContent content = itemGroup.getDisplayName().getContent();

                if ( content instanceof TranslatableTextContent translatableTextContent) {
                    builder.add(translatableTextContent.getKey(), translation);
                } else {
                    throw new UnsupportedOperationException(
                        "Cannot add language entry for ItemGroup (%s) as the display name is not translatable."
                            .formatted(itemGroup.getDisplayName().getString())
                    );
                }
            } else if (thing instanceof EntityType<?> entityType) {
                builder.add(entityType, translation);
            } else if (thing instanceof EntityAttribute entityAttribute) {
                RegistryEntry<EntityAttribute> registryEntry = RegistryEntry.of(entityAttribute);
                builder.add(registryEntry, translation);
            } else if (thing instanceof StatType<?> statType) {
                builder.add(statType, translation);
            } else if (thing instanceof StatusEffect effect) {
                builder.add(effect, translation);
            } else if (thing instanceof Identifier id) {
                builder.add(id, translation);
            } else if (thing instanceof TagKey<?> tagKey) {
                builder.add(tagKey, translation);
            } else if (thing instanceof SoundEvent sound) {
                builder.add(sound, translation);
            } else if (thing instanceof Path path) {
                try {
                    builder.add(path);
                } catch (IOException e) {
                    MOD_LOGGER.warn(
                        "WARNING! Failed to add translation for Path ({}): {}",
                        path.toString(), e.getMessage()
                    );
                }
            }
        }

    }
}
