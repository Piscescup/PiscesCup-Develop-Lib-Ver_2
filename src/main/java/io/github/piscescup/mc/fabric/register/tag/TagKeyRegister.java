package io.github.piscescup.mc.fabric.register.tag;

import io.github.piscescup.mc.fabric.datagen.tag.TagKeysContainer;
import io.github.piscescup.mc.fabric.register.Register;
import io.github.piscescup.mc.fabric.utils.CheckUtils.NullCheck;
import io.github.piscescup.mc.fabric.utils.constant.MCLanguageOption;
import net.minecraft.block.Block;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.PointOfInterestTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
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
 * <h2>Usages</h2>
 * Below is an example for Item Tags:
 * <pre>{@code
 * public static final TagKey<Item> ORES = TagKeyRegister.createFor(RegistryKeys.ITEM, MOD_ID, "ores")
 *     .addTag(ItemTags.COAL_ORES)
 *     .addTag(ItemTags.COPPER_ORES)
 *     .addTag(ItemTags.DIAMOND_ORES)
 *     .addTag(ItemTags.EMERALD_ORES)
 *     .addTag(ItemTags.GOLD_ORES)
 *     .addTag(ItemTags.IRON_ORES)
 *     .addTag(ItemTags.LAPIS_ORES)
 *     .addTag(ItemTags.REDSTONE_ORES)
 *     .register()
 *     .translate(MCLanguageOption.ZH_CN, "矿石")
 *     .translate(MCLanguageOption.EN_US, "Ores")
 *     .get();
 * }</pre>
 *
 * Below is an example for POI Tags:
 * <pre>{@code
 * public static final TagKey<PointOfInterestType> JOB = TagKeyRegister.VANILLA_ACQUIRABLE_JOB_SITE
 *     .addRegistryKey(PCDevLibTestPOIs.TEST_VILLAGER_POI)
 *     .register()
 *     .get();
 * }</pre>
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
    private final List<T> content;
    private final List<TagKey<T>> tags;
    private final List<RegistryKey<T>> keys;
    private final List<Function<RegistryWrapper.WrapperLookup, Stream<T>>> mappings;
    private final RegistryKey<? extends Registry<T>> registryRef;

    private TagKeyRegister(RegistryKey<? extends Registry<T>> registryRef, Identifier id) {
        super();
        this.id = id;
        this.thing = TagKey.of(registryRef, id);

        this.registryRef = registryRef;
        this.content = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.keys = new ArrayList<>();
        this.mappings = new ArrayList<>();
    }

    /**
     * Creates a new pre-registrable builder for a {@link TagKey} under the given registry reference.
     *
     * @param registryRef the registry this tag belongs to
     * @param id          the identifier of the tag
     * @param <T>         the element type contained by the tag
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
     */
    @Override
    public TagKeyPreRegistrable<T> addTag(@NotNull TagKey<T> tag) {
        NullCheck.requireNonNull(tag);
        this.tags.add(tag);
        return this;
    }

    @Override
    public TagKeyPreRegistrable<T> addRegistryKey(@NotNull RegistryKey<T> key) {
        NullCheck.requireNonNull(key);
        this.keys.add(key);
        return this;
    }

    /**
     * Adds a deferred mapping that produces elements to include in this tag.
     *
     * <p>The mapping is stored and is expected to be evaluated later with a
     * {@link RegistryWrapper.WrapperLookup} (typically during data generation).
     *
     * @param mappingFunction a lookup-dependent producer for tag elements
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
     */
    @Override
    public TagKeyPostRegistrable<T> register() {
        TagKeysContainer.getTagKeyRegisters(this.registryRef)
            .add(this);
        return this;
    }


    private static final BiFunction<Identifier, String, String> TAG_TRANSLATION_FUNC =
        (id, translation) -> "#" + id.getNamespace() + ":" + translation;

    /**
     * Adds a translation entry for this tag, prefixing the value with {@code "#"} and adding the namespace foe the translation.
     *
     * <p>This is a convenience for common tag-style UI labels (e.g., {@code "#pc-dev-lib:ores"}).
     *
     * @param lang  the language option
     * @param value the translation value.
     */
    @Override
    public TagKeyPostRegistrable<T> translate(@NotNull MCLanguageOption lang, @NotNull String value) {
        return super.translate(lang,  TAG_TRANSLATION_FUNC.apply(this.id, value));
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

    public List<RegistryKey<T>> getKeys() {
        return keys;
    }

    /**
     * Creates a new pre-registrable builder for a vanilla {@link TagKey}.
     *
     * @param <T> the element type contained by the tag
     * @param vanillaTag the vanilla tag to create a pre-registrable builder for
     * @return a new {@link TagKeyPreRegistrable} instance
     */
    @Contract("_ -> new")
    public static <T> @NotNull TagKeyPreRegistrable<T> createForTagKey(@NotNull TagKey<T> vanillaTag) {
        return new TagKeyRegister<>(vanillaTag.registryRef(), vanillaTag.id());
    }

    /**
     * Represents a pre-registrable tag key for blocks that require an iron tool or better to be mined.
     * This tag is designed to match the vanilla Minecraft behavior, where certain blocks are only
     * mineable with at least an iron-tier tool to prevent breaking and to drop their items.
     * It is used to categorize and manage blocks within the game's data system.
     */
    public static final TagKeyPreRegistrable<Block> VANILLA_NEEDS_IRON_TOOL = createForTagKey(BlockTags.NEEDS_IRON_TOOL);

    /**
     * Represents a pre-registrable tag key for blocks that require a diamond tool to be effectively mined.
     * This tag is designed to align with the vanilla Minecraft tag, ensuring compatibility and ease of use
     * within modded environments. Blocks associated with this tag will not drop anything or drop in a less
     * effective manner when mined with tools other than diamond ones.
     */
    public static final TagKeyPreRegistrable<Block> VANILLA_NEEDS_DIAMOND_TOOL = createForTagKey(BlockTags.NEEDS_DIAMOND_TOOL);

    /**
     * Represents a pre-registrable tag for blocks that require at least a stone tool to be mined effectively.
     * This tag is designed to align with the vanilla Minecraft behavior, specifically targeting blocks
     * associated with the {@link BlockTags#NEEDS_STONE_TOOL} tag.
     */
    public static final TagKeyPreRegistrable<Block> VANILLA_NEEDS_STONE_TOOL = createForTagKey(BlockTags.NEEDS_STONE_TOOL);

    /**
     * A pre-registrable builder for a vanilla {@link PointOfInterestType} tag, specifically for the "acquirable job site" tag.
     * This constant is used to create and manage tags related to point of interest types that can be acquired as job sites in the game.
     */
    public static final TagKeyPreRegistrable<PointOfInterestType> VANILLA_ACQUIRABLE_JOB_SITE = createForTagKey(PointOfInterestTypeTags.ACQUIRABLE_JOB_SITE);


}
