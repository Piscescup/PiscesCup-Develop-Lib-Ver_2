package io.github.piscescup.mc.fabric.register.item;

import io.github.piscescup.mc.fabric.register.Register;
import io.github.piscescup.mc.fabric.utils.CheckUtils.NullCheck;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * The {@code ItemRegister} class is used to register items with specified settings
 * and a factory for item creation. This class implements both {@link ItemPreRegistrable}
 * and {@link ItemPostRegistrable} interfaces, allowing for pre-registration setup and
 * post-registration actions.
 * <p>
 * The {@code ItemRegister} supports item creation with a customizable {@code Item.Settings}
 * and allows the definition of a custom factory for item instantiation.
 * </p>
 *
 * <h2>Usages</h2>
 * <pre>{@code
 * public static final Item TEST_ITEM1 = ItemRegister.createForItem(MOD_ID, "test_item1")
 *         .setting(new Item.Settings()
 *             .maxCount(1)
 *             .fireproof()
 *         )
 *         .register()
 *         .translate(MCLanguage.EN_US, "PCDevLib Test Item 1")
 *         .translate(MCLanguage.ZH_CN, "PCDevLib 测试物品 1")
 *         .get();
 * }</pre>
 * @author REN YuanTong
 * @since 1.0.0
 */
public class ItemRegister
    extends Register<Item, ItemPostRegistrable, ItemRegister>
    implements ItemPreRegistrable, ItemPostRegistrable
{
    private Item.Settings settings =  new Item.Settings();
    private Function<Item.Settings, Item> factory = Item::new;

    /**
     * Private constructor for creating an {@code ItemRegister} with a specific identifier.
     *
     * @param id The identifier for the item being registered.
     */
    private ItemRegister(Identifier id) {
        super(RegistryKeys.ITEM, id);
    }

    /**
     * Creates a {@code PreItemRegisterable} for item registration with the given identifier.
     *
     * @param id The identifier of the item.
     * @return A {@code PreItemRegisterable} for the item with the given identifier.
     */
    @Contract("_ -> new")
    public static @NotNull ItemPreRegistrable createForItem(@NotNull Identifier id) {
        NullCheck.requireNonNull(id);
        return new ItemRegister(id);
    }

    /**
     * Creates a {@code PreItemRegisterable} for item registration with the given specified namespace and path.
     *
     * @param namespace The namespace of the item.
     * @param path The path of the item.
     * @return A {@code PreItemRegisterable} for the item with the given specified namespace and path.
     */
    @Contract("_, _ -> new")
    public static @NotNull ItemPreRegistrable createForItem(@NotNull String namespace, @NotNull String path) {
        NullCheck.requireNonNull(namespace);
        NullCheck.requireNonNull(path);
        return new ItemRegister(Identifier.of(namespace, path));
    }

    /**
     * Sets the settings for the item being registered. The settings provide configuration options
     * for the item, such as its attributes and behavior.
     *
     * @param settings The settings to apply to the item.
     * @return The current {@code PreItemRegisterable} instance, allowing for method chaining.
     * @see Item.Settings
     */
    @Override
    public ItemPreRegistrable setting(@NotNull Item.Settings settings) {
        NullCheck.requireNonNull(settings);
        this.settings = settings;
        return this;
    }

    /**
     * Sets a custom factory for creating the item with the specified settings.
     * The factory takes {@code Item.Settings} as input and returns a newly created {@code Item}.
     *
     * @param factory A function that creates an item based on the given settings.
     * @return The current {@code PreItemRegisterable} instance, allowing for method chaining.
     */
    @Override
    public ItemPreRegistrable factory(@NotNull Function<Item.Settings, Item> factory) {
        NullCheck.requireNonNull(factory);
        this.factory = factory;
        return this;
    }

    /**
     * Registers the item after the pre-registration setup is completed. This method uses the
     * specified settings and factory to create an item, then registers it with the registry.
     *
     * @return The current {@code PostItemRegisterable} instance, allowing for method chaining.
     */
    @Override
    public ItemPostRegistrable register() {
        Item item = factory.apply(settings.registryKey(this.registryKey));

        // If the item is a BlockItem, append it to the block items registry.
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }

        // Register the item with the registry.
        this.thing = Registry.register(Registries.ITEM, this.id, item);

        return this;
    }
}
