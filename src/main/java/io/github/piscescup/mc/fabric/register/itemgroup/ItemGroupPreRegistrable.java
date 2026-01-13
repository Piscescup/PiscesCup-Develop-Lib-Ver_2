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
 * A pre-registration builder API for creating and registering an
 * {@link ItemGroup}.
 *
 * <p>This interface provides a staged, fluent configuration pipeline for
 * defining all properties required to construct an {@link ItemGroup},
 * including its position, icon, visual appearance, background texture,
 * and displayed entries.
 *
 * <p>The staged design enforces the correct configuration order at
 * compile time, ensuring that all mandatory attributes are supplied
 * before registration occurs.
 *
 * <p>Methods return the next stage of the builder to enable fluent and
 * type-safe chaining.
 *
 * <p>Intended for use in Fabric-based mod development.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface ItemGroupPreRegistrable
{
    /**
     * Builder stage for configuring the position of the {@link ItemGroup}
     * within the creative inventory tab layout.
     *
     * <p>This stage defines where the item group tab will appear by specifying
     * its row and column.
     *
     * <p>Methods return the next builder stage to enable fluent chaining.
     *
     * @since 1.0.0
     */
    interface PositionStage {

        /**
         * Sets the row and column position of the item group tab.
         *
         * @param row    the creative inventory row
         * @param column the column index within the row
         * @return the next stage for icon configuration
         */
        IconStage position(ItemGroup.Row row, int column);
    }

    /**
     * Builder stage for configuring the icon of the {@link ItemGroup}.
     *
     * <p>The icon is displayed in the creative inventory tab header.
     * Implementations may supply the icon lazily via a {@link Supplier}.
     *
     * <p>Methods return the next builder stage to enable fluent chaining.
     *
     * @since 1.0.0
     */
    interface IconStage {

        /**
         * Sets the icon supplier used to create the tab icon.
         *
         * @param iconSupplier supplier producing the icon {@link ItemStack}
         * @return the next stage for appearance configuration
         * @throws NullPointerException if {@code iconSupplier} is null
         */
        AppearanceStage icon(@NotNull Supplier<ItemStack> iconSupplier);

        /**
         * Sets the icon using an {@link ItemConvertible}.
         *
         * @param item the item used as the icon
         * @return the next stage for appearance configuration
         * @throws NullPointerException if {@code item} is null
         */
        default AppearanceStage icon(@NotNull ItemConvertible item) {
            NullCheck.requireNonNull(item);
            return this.icon(() -> new ItemStack(item));
        }

        /**
         * Sets the icon using a predefined {@link ItemStack}.
         *
         * <p>The provided stack will be copied internally.
         *
         * @param icon the icon stack
         * @return the next stage for appearance configuration
         * @throws NullPointerException if {@code icon} is null
         */
        default AppearanceStage icon(@NotNull ItemStack icon) {
            NullCheck.requireNonNull(icon);
            return this.icon(() -> icon.copy());
        }

        /**
         * Sets the icon using an {@link Item}.
         *
         * @param item the item used as the icon
         * @return the next stage for appearance configuration
         * @throws NullPointerException if {@code item} is null
         */
        default AppearanceStage icon(@NotNull Item item) {
            NullCheck.requireNonNull(item);
            return this.icon(() -> new ItemStack(item));
        }

        /**
         * Uses {@link ItemStack#EMPTY} as the tab icon.
         *
         * @return the next stage for appearance configuration
         */
        default AppearanceStage emptyIcon() {
            return this.icon(() -> ItemStack.EMPTY);
        }
    }

    /**
     * Builder stage for configuring visual appearance options of the
     * {@link ItemGroup}.
     *
     * <p>This stage controls UI-related flags such as scrollbar visibility
     * and display name rendering.
     *
     * <p>Methods return the next builder stage to enable fluent chaining.
     *
     * @since 1.0.0
     */
    interface AppearanceStage {

        /**
         * Configures appearance-related flags of the item group.
         *
         * @param scrollbar         whether the scrollbar is enabled
         * @param renderDisplayName whether the display name is rendered
         * @param specialItemGroup  whether the group is treated as special
         * @return the next stage for texture configuration
         */
        TextureStage appearance(boolean scrollbar,
                                boolean renderDisplayName,
                                boolean specialItemGroup);

        /**
         * Applies the default appearance configuration.
         *
         * @return the next stage for texture configuration
         */
        default TextureStage defaultAppearance() {
            return this.appearance(true, true, false);
        }

        /**
         * Applies the default appearance configuration and marks the group as special.
         *
         * @return the next stage for texture configuration
         */
        default TextureStage specialAppearance() {
            return this.appearance(true, true, true);
        }
    }

    /**
     * Builder stage for configuring the background texture of the
     * {@link ItemGroup} tab.
     *
     * <p>This stage determines which texture is used when rendering
     * the creative inventory tab.
     *
     * <p>Methods return the next builder stage to enable fluent chaining.
     *
     * @since 1.0.0
     */
    interface TextureStage {

        /**
         * The default vanilla item group tab texture.
         */
        Identifier ITEMS = getVanillaTabTextureId("items");

        /**
         * Sets the background texture used by the item group tab.
         *
         * @param texture the texture identifier
         * @return the next stage for entry collection
         * @throws NullPointerException if {@code texture} is null
         */
        EntryCollectStage texture(@NotNull Identifier texture);

        /**
         * Uses the default vanilla item group tab texture.
         *
         * @return the next stage for entry collection
         */
        default EntryCollectStage defaultTexture() {
            return this.texture(ITEMS);
        }

        /**
         * Resolves the identifier of a vanilla creative tab texture.
         *
         * @param name the vanilla tab name
         * @return the resolved texture identifier
         */
        static Identifier getVanillaTabTextureId(@NotNull String name) {
            return Identifier.ofVanilla(
                "textures/gui/container/creative_inventory/tab_" + name + ".png"
            );
        }
    }

    /**
     * Builder stage for collecting item entries and completing
     * the {@link ItemGroup} registration process.
     *
     * <p>This stage allows adding item entries in multiple forms and
     * supports both direct entry addition and context-based collection.
     *
     * <p>Once all entries are defined, registration can be finalized
     * via {@link PreRegistrable}.
     *
     * <p>Methods return this stage to enable fluent chaining.
     *
     * @since 1.0.0
     */
    interface EntryCollectStage
        extends PreRegistrable<ItemGroupPostRegistrable>
    {
        /**
         * Adds a single {@link ItemStack} entry with explicit visibility.
         *
         * @param stack      the item stack to add
         * @param visibility the visibility of the stack
         * @return this stage for method chaining
         */
        EntryCollectStage addStackEntry(
            @NotNull ItemStack stack,
            ItemGroup.StackVisibility visibility
        );

        /**
         * Adds a single {@link ItemStack} entry with default visibility.
         *
         * @param item the item stack to add
         * @return this stage for method chaining
         */
        default EntryCollectStage addStackEntry(@NotNull ItemStack item) {
            NullCheck.requireNonNull(item);
            return this.addStackEntry(item, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        /**
         * Adds multiple {@link ItemStack} entries.
         *
         * @param items      the item stacks to add
         * @param visibility the visibility of the stacks
         * @return this stage for method chaining
         */
        EntryCollectStage addStackEntries(
            @NotNull Collection<@NotNull ItemStack> items,
            ItemGroup.StackVisibility visibility
        );

        /**
         * Adds multiple {@link ItemStack} entries with default visibility.
         *
         * @param items the item stacks to add
         * @return this stage for method chaining
         */
        default EntryCollectStage addStackEntries(
            @NotNull Collection<@NotNull ItemStack> items
        ) {
            NullCheck.requireAllNonNull(items);
            return this.addStackEntries(items, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        /**
         * Collects entries using a raw {@link ItemGroup.EntryCollector}.
         *
         * @param collector the entry collector
         * @return this stage for method chaining
         */
        EntryCollectStage collectBy(@NotNull ItemGroup.EntryCollector collector);

        /**
         * Collects entries using a {@link ItemGroup.DisplayContext}-aware action.
         *
         * @param action the context-based collection logic
         * @return this stage for method chaining
         */
        EntryCollectStage collectByContext(
            @NotNull Consumer<ItemGroup.DisplayContext> action
        );
    }
}
