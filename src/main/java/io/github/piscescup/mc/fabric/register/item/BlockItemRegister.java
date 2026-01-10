package io.github.piscescup.mc.fabric.register.item;

import io.github.piscescup.mc.fabric.register.Register;
import io.github.piscescup.mc.fabric.utils.CheckUtils.NullCheck;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

/**
 * A register helper that constructs and registers an {@link Item} corresponding to a {@link Block}.
 *
 * <p>This class provides a fluent pre-registration API to configure the {@link Item.Settings}
 * and an item factory, then performs the actual registration in the {@link #register()} step.
 *
 * <p>Typical usage:
 * <pre>
 * BlockItemRegister.createFor(block)
 *     .setting(new Item.Settings()...)
 *     .factory(CustomBlockItem::new) // Optional custom factory
 *     .register();
 * </pre>
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class BlockItemRegister
    extends Register<Item, BlockItemPostRegistrable, BlockItemRegister>
    implements BlockItemPreRegistrable, BlockItemPostRegistrable
{
    private Block block;

    private Item.Settings settings = new Item.Settings();

    private BiFunction<Block, Item.Settings, Item> factory = BlockItem::new;

    private BlockItemRegister(@NotNull Block block) {
        Identifier id = block.getRegistryEntry().registryKey().getValue();
        super(RegistryKeys.ITEM, id);
        this.block = block;
    }

    /**
     * Create a new pre-registrable builder for the given {@link Block}.
     *
     * @param block The block to create an item for; must not be null.
     * @throws NullPointerException if {@code block} is null
     */
    @Contract("_ -> new")
    public static @NotNull BlockItemPreRegistrable createFor(@NotNull Block block) {
        NullCheck.requireNonNull(block);
        return new BlockItemRegister(block);
    }

    /**
     * Configure the {@link Item.Settings} used when the item is constructed.
     *
     * <p>The provided settings will be modified by calling
     * {@link Item.Settings#useBlockPrefixedTranslationKey()} before being stored.
     *
     * @param settings The item settings to apply; must not be null.
     * @throws NullPointerException If {@code settings} is null.
     */
    @Override
    public BlockItemPreRegistrable setting(Item.@NotNull Settings settings) {
        NullCheck.requireNonNull(settings);
        this.settings = settings;
        return this;
    }

    /**
     * Provide a factory function that will be used to create the {@link Item} during registration.
     *
     * @param factory A bi-function that accepts the {@link Block} and {@link Item.Settings} and returns an {@link Item}; must not be null
     * @throws NullPointerException If {@code factory} is null.
     */
    @Override
    public BlockItemPreRegistrable factory(@NotNull BiFunction<Block, Item.Settings, Item> factory) {
        NullCheck.requireNonNull(factory);
        this.factory = factory;
        return this;
    }

    /**
     * Performs the registration: derives the item registry key from the block, applies the key
     * to the settings, creates the item via the factory, updates block-item mappings if necessary,
     * and registers the item in the item registry.
     */
    @Override
    public BlockItemPostRegistrable register() {
        this.settings
            .useBlockPrefixedTranslationKey()
            .registryKey(this.registryKey);

        Item item = this.factory.apply(this.block, settings);

        if  (item instanceof BlockItem blockItem)
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);

        this.thing = Registry.register(
            Registries.ITEM,
            this.registryKey,
            item
        );

        return this;
    }
}