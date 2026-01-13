package io.github.piscescup.mc.fabric.register.itemgroup;

import io.github.piscescup.mc.fabric.register.PreRegistrable;
import io.github.piscescup.mc.fabric.utils.CheckUtils.NullCheck;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * A staged pre-registration builder API for creating and registering an {@link ItemGroup}.
 *
 * <p>This interface implements a <b>fluent builder pattern with compile-time enforced ordering</b>,
 * ensuring that {@link ItemGroup} instances are configured in the correct sequence:
 *
 * <pre>
 * Position → Icon → Appearance → Texture → Entry Collection → Registration
 * </pre>
 *
 * <p>Each nested stage interface represents a mandatory configuration step that must be completed
 * before proceeding to the next. Skipping or reordering stages is prevented at compile time.
 *
 * <p>Designed for Fabric mod development.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface ItemGroupPreRegistrable
{
    /**
     * Stage for configuring the position of the item group within the creative inventory tab grid.
     */
    interface PositionStage {

        /**
         * Sets the row and column position of the item group tab.
         *
         * @param row    the creative tab row
         * @param column the column index within the row
         * @return next stage for icon configuration
         * @throws NullPointerException if {@code row} is null
         */
        IconStage position(ItemGroup.Row row, int column);
    }

    /**
     * Stage for defining the icon of the item group.
     */
    interface IconStage {

        /**
         * Sets the icon supplier for this item group.
         *
         * @param iconSupplier a supplier that produces the icon {@link ItemStack}; must not be null
         * @return next stage for appearance configuration
         * @throws NullPointerException if {@code iconSupplier} is null
         */
        AppearanceStage icon(@NotNull Supplier<ItemStack> iconSupplier);

        /**
         * Sets the icon using an {@link ItemConvertible}.
         *
         * @param item the item used as icon; must not be null
         * @return next stage for appearance configuration
         * @throws NullPointerException if {@code item} is null
         */
        default AppearanceStage icon(@NotNull ItemConvertible item) {
            NullCheck.requireNonNull(item);
            return this.icon(() -> new ItemStack(item));
        }

        /**
         * Sets the icon using a prebuilt {@link ItemStack}.
         * The provided stack will be copied internally.
         *
         * @param icon the icon stack; must not be null
         * @return next stage for appearance configuration
         * @throws NullPointerException if {@code icon} is null
         */
        default AppearanceStage icon(@NotNull ItemStack icon) {
            NullCheck.requireNonNull(icon);
            return this.icon(() -> icon.copy());
        }

        /**
         * Sets the icon using an {@link Item}.
         *
         * @param item the item used as icon; must not be null
         * @return next stage for appearance configuration
         * @throws NullPointerException if {@code item} is null
         */
        default AppearanceStage icon(@NotNull Item item) {
            NullCheck.requireNonNull(item);
            return this.icon(() -> new ItemStack(item));
        }

        /**
         * Uses {@link ItemStack#EMPTY} as the icon.
         *
         * @return next stage for appearance configuration
         */
        default AppearanceStage emptyIcon() {
            return this.icon(() -> ItemStack.EMPTY);
        }
    }

    /**
     * Stage for configuring visual appearance options of the item group.
     */
    interface AppearanceStage {

        /**
         * Configures the visual appearance flags of the item group.
         *
         * @param scrollbar           whether to show the scrollbar
         * @param renderDisplayName   whether to render the display name
         * @param specialItemGroup    whether this is treated as a special group
         * @return next stage for texture configuration
         */
        TextureStage appearance(boolean scrollbar,
                                boolean renderDisplayName,
                                boolean specialItemGroup);

        /**
         * Applies the default appearance configuration.
         *
         * @return next stage for texture configuration
         */
        default TextureStage defaultAppearance() {
            return this.appearance(true, true, false);
        }

        /**
         * Applies the default appearance but marks the group as special.
         *
         * @return next stage for texture configuration
         */
        default TextureStage specialAppearance() {
            return this.appearance(true, true, true);
        }
    }

    /**
     * Stage for configuring the tab background texture.
     */
    interface TextureStage {

        /**
         * Default vanilla item group texture identifier.
         */
        Identifier ITEMS = getVanillaTabTextureId("items");

        /**
         * Sets the background texture used for this item group tab.
         *
         * @param texture texture identifier; must not be null
         * @return next stage for entry collection
         * @throws NullPointerException if {@code texture} is null
         */
        EntryCollectStage texture(@NotNull Identifier texture);

        /**
         * Uses the default vanilla item group texture.
         *
         * @return next stage for entry collection
         */
        default EntryCollectStage defaultTexture() {
            return this.texture(ITEMS);
        }

        /**
         * Resolves a vanilla creative tab texture identifier.
         *
         * @param name vanilla tab name; must not be null
         * @return texture identifier
         * @throws NullPointerException if {@code name} is null
         */
        static Identifier getVanillaTabTextureId(@NotNull String name) {
            return Identifier.ofVanilla(
                "textures/gui/container/creative_inventory/tab_" + name + ".png"
            );
        }
    }

    /**
     * Stage for collecting item entries and finalizing registration.
     */
    interface EntryCollectStage
        extends PreRegistrable<ItemGroupPostRegistrable>
    {
        /**
         * Adds a single item stack entry with explicit visibility.
         *
         * @param stack       item stack to add; must not be null
         * @param visibility  stack visibility; must not be null
         * @return this stage for chaining
         * @throws NullPointerException if {@code stack} or {@code visibility} is null
         */
        EntryCollectStage addStackEntry(
            @NotNull ItemStack stack,
            ItemGroup.StackVisibility visibility
        );

        /**
         * Adds a single item stack with default visibility.
         *
         * @param item item stack to add; must not be null
         * @return this stage for chaining
         * @throws NullPointerException if {@code item} is null
         */
        default EntryCollectStage addStackEntry(@NotNull ItemStack item) {
            NullCheck.requireNonNull(item);
            return this.addStackEntry(item, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        /**
         * Adds an {@link ItemConvertible} entry with specified visibility.
         *
         * @param item        item to add; must not be null
         * @param visibility  stack visibility; must not be null
         * @return this stage for chaining
         * @throws NullPointerException if {@code item} or {@code visibility} is null
         */
        default EntryCollectStage addItemConvertibleEntry(
            @NotNull ItemConvertible item,
            ItemGroup.StackVisibility visibility
        ) {
            NullCheck.requireNonNull(item);
            NullCheck.requireNonNull(visibility);
            return this.addStackEntry(new ItemStack(item), visibility);
        }

        /**
         * Adds an {@link ItemConvertible} entry with default visibility.
         *
         * @param item item to add; must not be null
         * @return this stage for chaining
         * @throws NullPointerException if {@code item} is null
         */
        default EntryCollectStage addItemConvertibleEntry(@NotNull ItemConvertible item) {
            NullCheck.requireNonNull(item);
            return this.addItemConvertibleEntry(item, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        /**
         * Adds multiple {@link ItemStack} entries with specified visibility.
         *
         * @param items       item stacks; must not be null and must not contain null elements
         * @param visibility  stack visibility; must not be null
         * @return this stage for chaining
         * @throws NullPointerException if {@code items} or {@code visibility} is null, or if {@code items} contains null
         */
        EntryCollectStage addStackEntries(
            @NotNull Collection<@NotNull ItemStack> items,
            ItemGroup.StackVisibility visibility
        );

        /**
         * Adds multiple {@link ItemStack} entries with default visibility.
         *
         * @param items item stacks; must not be null and must not contain null elements
         * @return this stage for chaining
         * @throws NullPointerException if {@code items} is null or contains null
         */
        default EntryCollectStage addStackEntries(@NotNull Collection<@NotNull ItemStack> items) {
            NullCheck.requireAllNonNull(items);
            return this.addStackEntries(items, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        /**
         * Adds multiple {@link ItemConvertible} entries with specified visibility.
         *
         * @param items       items to add; must not be null and must not contain null elements
         * @param visibility  stack visibility; must not be null
         * @return this stage for chaining
         * @throws NullPointerException if {@code items} or {@code visibility} is null, or if {@code items} contains null
         */
        default EntryCollectStage addItemConvertibleEntries(
            @NotNull Collection<@NotNull ItemConvertible> items,
            ItemGroup.StackVisibility visibility
        ) {
            NullCheck.requireAllNonNull(items);
            NullCheck.requireNonNull(visibility);

            List<ItemStack> stacks = items.stream()
                .map(ItemStack::new)
                .toList();

            return this.addStackEntries(stacks, visibility);
        }

        /**
         * Adds multiple {@link ItemConvertible} entries with default visibility.
         *
         * @param items items to add; must not be null and must not contain null elements
         * @return this stage for chaining
         * @throws NullPointerException if {@code items} is null or contains null
         */
        default EntryCollectStage addItemConvertibleEntries(
            @NotNull Collection<@NotNull ItemConvertible> items
        ) {
            return this.addItemConvertibleEntries(
                items,
                ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS
            );
        }

        /**
         * Adds multiple {@link Item} entries with specified visibility.
         *
         * @param items       items to add; must not be null and must not contain null elements
         * @param visibility  stack visibility; must not be null
         * @return this stage for chaining
         * @throws NullPointerException if {@code items} or {@code visibility} is null, or if {@code items} contains null
         */
        default EntryCollectStage addItemEntries(@NotNull Collection<Item> items,
                                                 ItemGroup.StackVisibility visibility) {
            NullCheck.requireAllNonNull(items);
            NullCheck.requireNonNull(visibility);

            List<ItemStack> stacks = items.stream()
                .map(ItemStack::new)
                .toList();

            return this.addStackEntries(stacks, visibility);
        }

        /**
         * Adds multiple {@link Item} entries with default visibility.
         *
         * @param items items to add; must not be null and must not contain null elements
         * @return this stage for chaining
         * @throws NullPointerException if {@code items} is null or contains null
         */
        default EntryCollectStage addItemEntries(@NotNull Collection<@NotNull Item> items) {
            return this.addItemEntries(items, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        /**
         * Collects entries using a raw {@link ItemGroup.EntryCollector}.
         *
         * @param collector collector callback; must not be null
         * @return this stage for chaining
         * @throws NullPointerException if {@code collector} is null
         */
        EntryCollectStage collectBy(@NotNull ItemGroup.EntryCollector collector);

        /**
         * Collects entries using a display context consumer.
         *
         * @param action context-based collector; must not be null
         * @return this stage for chaining
         * @throws NullPointerException if {@code action} is null
         */
        EntryCollectStage collectByContext(
            @NotNull Consumer<ItemGroup.DisplayContext> action
        );
    }
}