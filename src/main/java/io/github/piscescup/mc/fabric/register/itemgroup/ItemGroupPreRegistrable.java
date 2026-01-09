package io.github.piscescup.mc.fabric.register.itemgroup;

import io.github.piscescup.mc.fabric.register.PreRegistrable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <h2>Description</h2>
 *
 * <h2>Usages</h2>
 *
 * @author REN YuanTong
 * @Date 2026-01-04
 * @since 1.0.0
 */
public interface ItemGroupPreRegistrable
{
    interface PositionStage {
        DisplayNameStage position(ItemGroup.Row row, int column);
    }

    interface DisplayNameStage {
        IconStage displayName(Supplier<Text> displayNameSupplier);

        default IconStage displayName(String displayName) {
            return this.displayName( () -> Text.literal(displayName) );
        }

        default IconStage displayName(Text displayName) {
            return this.displayName( () -> displayName );
        }

        default IconStage emptyDisplayName() {
            return this.displayName(Text::empty);
        }
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
        ItemGroupEntryCollectable appearance(
            boolean scrollbar,
            boolean renderName,
            boolean special,
            ItemGroup.Type type
        );
    }

    interface ItemGroupEntryCollectable
        extends PreRegistrable<ItemGroupPostRegistrable>
    {
        ItemGroupEntryCollectable entry(ItemStack stack, ItemGroup.StackVisibility visibility);

        default ItemGroupEntryCollectable entry(ItemConvertible item) {
            return this.entry(new ItemStack(item), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        default ItemGroupEntryCollectable entry(ItemStack itemStack) {
            return this.entry(itemStack, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        ItemGroupEntryCollectable entries(Collection<? extends ItemConvertible> items, ItemGroup.StackVisibility visibility);

        default ItemGroupEntryCollectable entries(Collection<? extends ItemConvertible> items) {
            return this.entries(items, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        ItemGroupEntryCollectable collectBy(ItemGroup.EntryCollector collector);

        default ItemGroupEntryCollectable collectByContext(Consumer<ItemGroup.DisplayContext> action) {
            return this.collectBy(
                (displayContext, entries) -> action.accept(displayContext)
            );
        }

    }
}
