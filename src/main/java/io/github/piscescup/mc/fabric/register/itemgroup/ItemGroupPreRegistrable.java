package io.github.piscescup.mc.fabric.register.itemgroup;

import io.github.piscescup.mc.fabric.register.PreRegistrable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 *
 * @author REN YuanTong
 * @Date 2026-01-04
 * @since 1.0.0
 */
public interface ItemGroupPreRegistrable
{
    interface PositionStage {
        IconStage position(ItemGroup.Row row, int column);
    }

    interface IconStage {
        AppearanceStage icon(Supplier<ItemStack> iconSupplier);

        default AppearanceStage icon(ItemConvertible item) {
            return this.icon( () -> new ItemStack(item));
        }

        default AppearanceStage icon(ItemStack icon) {
            return this.icon( () -> icon );
        }

        default AppearanceStage icon(Item item) {
            return this.icon( () -> new ItemStack(item));
        }

        default AppearanceStage emptyIcon() {
            return this.icon( () -> ItemStack.EMPTY );
        }
    }

    interface AppearanceStage {
        TextureStage appearance(
            boolean scrollbar,
            boolean renderName,
            boolean special
        );

        default TextureStage defaultAppearance() {
            return this.appearance(true, true, false);
        }
    }

    interface TextureStage {
        Identifier ITEMS = getTabTextureId("items");

        EntryCollectStage texture(Identifier texture);

        default EntryCollectStage defaultTexture() {
            return this.texture(ITEMS);
        }

        static Identifier getTabTextureId(String name) {
            return Identifier.ofVanilla("textures/gui/container/creative_inventory/tab_" + name + ".png");
        }
    }

    interface EntryCollectStage
        extends PreRegistrable<ItemGroupPostRegistrable>
    {
        EntryCollectStage entry(ItemStack stack, ItemGroup.StackVisibility visibility);

        default EntryCollectStage entry(ItemConvertible item) {
            return this.entry(new ItemStack(item), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        default EntryCollectStage entry(ItemStack itemStack) {
            return this.entry(itemStack, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        EntryCollectStage stackEntries(Collection<ItemStack> stacks, ItemGroup.StackVisibility visibility);

        default EntryCollectStage itemEntries(Collection<ItemConvertible> items, ItemGroup.StackVisibility visibility) {
            Objects.requireNonNull(items);
            List<ItemStack> stacks = items.stream()
                .filter(Objects::nonNull)
                .map(ItemStack::new)
                .toList();
            return this.stackEntries(stacks, visibility);
        }


        default EntryCollectStage itemEntries(Collection<ItemConvertible> items) {
            return this.itemEntries(items, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        default EntryCollectStage stackEntries(Collection<ItemStack> stacks) {
            return this.stackEntries(stacks, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        EntryCollectStage collectBy(ItemGroup.EntryCollector collector);

        EntryCollectStage collectByContext(Consumer<ItemGroup.DisplayContext> action);

    }
}
