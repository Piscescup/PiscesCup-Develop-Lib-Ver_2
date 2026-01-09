package io.github.piscescup.mc.fabric.register.itemgroup;

import io.github.piscescup.mc.fabric.register.Register;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

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
public class ItemGroupRegister
    extends Register<ItemGroup, ItemGroupPostRegistrable, ItemGroupRegister>
{
    private static final ItemGroup.EntryCollector EMPTY_ENTRIES = (displayContext, entries) -> {};
    private ItemGroup.Row row;
    private int column;
    private Text displayName = Text.empty();
    private Supplier<ItemStack> iconSupplier = () -> ItemStack.EMPTY;
    private ItemGroup.EntryCollector entryCollector = EMPTY_ENTRIES;
    private boolean scrollbar = true;
    private boolean renderName = true;
    private boolean special = false;
    private ItemGroup.Type type = ItemGroup.Type.CATEGORY;

    private ItemGroupRegister(Identifier id) {
        this.id = id;
    }
}
