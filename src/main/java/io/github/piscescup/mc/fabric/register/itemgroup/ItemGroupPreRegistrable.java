package io.github.piscescup.mc.fabric.register.itemgroup;

import io.github.piscescup.mc.fabric.register.PreRegistrable;
import io.github.piscescup.mc.fabric.util.CheckUtil;
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
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface ItemGroupPreRegistrable
{
    interface PositionStage {
        IconStage position(ItemGroup.Row row, int column);
    }

    interface IconStage {
        AppearanceStage icon(@NotNull Supplier<ItemStack> iconSupplier);

        default AppearanceStage icon(@NotNull ItemConvertible item) {
            CheckUtil.NullCheck.requireNotNull(item);
            return this.icon( () -> new ItemStack(item));
        }

        default AppearanceStage icon(@NotNull ItemStack icon) {
            CheckUtil.NullCheck.requireNotNull(icon);
            return this.icon( () -> icon.copy() );
        }

        default AppearanceStage icon(@NotNull Item item) {
            CheckUtil.NullCheck.requireNotNull(item);
            return this.icon( () -> new ItemStack(item));
        }

        default AppearanceStage emptyIcon() {
            return this.icon( () -> ItemStack.EMPTY );
        }

    }

    interface AppearanceStage {
        TextureStage appearance(boolean scrollbar, boolean renderDisplayName, boolean specialItemGroup);

        default TextureStage defaultAppearance() {
            return this.appearance(true, true, false);
        }

        default TextureStage specialAppearance() {
            return this.appearance(true, true, true);
        }
    }

    interface TextureStage {
        static final Identifier ITEMS = getVanillaTabTextureId("items");

        EntryCollectStage texture(@NotNull Identifier texture);

        default EntryCollectStage defaultTexture() {
            return this.texture(ITEMS);
        }

        static Identifier getVanillaTabTextureId(@NotNull String name) {
            return Identifier.ofVanilla("textures/gui/container/creative_inventory/tab_" + name + ".png");
        }
    }

    interface EntryCollectStage
        extends PreRegistrable<ItemGroupPostRegistrable>
    {
        EntryCollectStage addEntry(@NotNull ItemStack stack, ItemGroup.StackVisibility visibility);

        default EntryCollectStage addEntry(@NotNull ItemStack item) {
            CheckUtil.NullCheck.requireNotNull(item);
            return this.addEntry(item, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        default EntryCollectStage addEntry(@NotNull ItemConvertible item, ItemGroup.StackVisibility visibility) {
            CheckUtil.NullCheck.requireNotNull(item);
            return this.addEntry(new ItemStack(item), visibility);
        }

        default EntryCollectStage addEntry(@NotNull ItemConvertible item) {
            CheckUtil.NullCheck.requireNotNull(item);
            return this.addEntry(item, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        EntryCollectStage addStackEntries(@NotNull Collection<@NotNull ItemStack> items, ItemGroup.StackVisibility visibility);

        default EntryCollectStage addStackEntries(@NotNull Collection<@NotNull ItemStack> items) {
            CheckUtil.NullCheck.requireAllNotNull(items);
            return this.addStackEntries(items, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        default EntryCollectStage addItemConvertibleEntries(@NotNull Collection<@NotNull ItemConvertible> items, ItemGroup.StackVisibility visibility) {
            CheckUtil.NullCheck.requireAllNotNull(items);
            CheckUtil.NullCheck.requireNotNull(visibility);
            List<ItemStack> stacks = items.stream()
                .filter(CheckUtil.NullCheck::nonNull)
                .map(ItemStack::new)
                .toList();

            return this.addStackEntries(stacks, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        default EntryCollectStage addItemConvertibleEntries(@NotNull Collection<@NotNull ItemConvertible> items) {

            return this.addItemConvertibleEntries(items, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        default EntryCollectStage addItemEntries(@NotNull Collection<@NotNull Item> items, ItemGroup.StackVisibility visibility) {
            CheckUtil.NullCheck.requireAllNotNull(items);
            CheckUtil.NullCheck.requireNotNull(visibility);
            List<ItemStack> stacks = items.stream()
                .filter(CheckUtil.NullCheck::nonNull)
                .map(ItemStack::new)
                .toList();

            return this.addStackEntries(stacks, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        default EntryCollectStage addItemEntries(@NotNull Collection<@NotNull Item> items) {
            return this.addItemEntries(items, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        EntryCollectStage collectBy(@NotNull ItemGroup.EntryCollector collector);

        EntryCollectStage collectByContext(@NotNull Consumer<ItemGroup.DisplayContext> action);

    }
}
