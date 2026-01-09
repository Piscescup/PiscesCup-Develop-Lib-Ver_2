package io.github.piscescup.mc.fabric.register.itemgroup;

import io.github.piscescup.mc.fabric.register.Register;
import io.github.piscescup.mc.fabric.utils.ItemGroupCollectorUtils;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
public class ItemGroupRegister
    extends Register<ItemGroup, ItemGroupPostRegistrable, ItemGroupRegister>
    implements ItemGroupPreRegistrable.PositionStage, ItemGroupPreRegistrable.IconStage,
                ItemGroupPreRegistrable.AppearanceStage, ItemGroupPreRegistrable.TextureStage,
                ItemGroupPreRegistrable.EntryCollectStage,
                ItemGroupPostRegistrable
{
    private ItemGroup.Row row;
    private int column;
    private Text displayName;
    private Supplier<ItemStack> iconSupplier;
    private boolean scrollbar;
    private boolean renderName;
    private boolean special;
    private Identifier texture;
    private ItemGroup.Type type;

    private ItemGroup.EntryCollector entryCollector;
    private Map<ItemStack, ItemGroup.StackVisibility> entries;
    private List<Consumer<ItemGroup.DisplayContext>> collectorsByContext;

    private ItemGroupRegister(Identifier id) {
        super(RegistryKeys.ITEM_GROUP, id);
        String translationKey = Util.createTranslationKey("itemGroup", id);
        this.displayName = Text.translatable(translationKey);
    }

    @Override
    public ItemGroupPostRegistrable register() {
        ItemGroup.EntryCollector collectorByContext =
            ItemGroupCollectorUtils.scheduleContextCollector(collectorsByContext);

        ItemGroup.EntryCollector finalCollector =
            ItemGroupCollectorUtils.andThen(this.entryCollector, collectorByContext);

        ItemGroup.Builder builder = ItemGroup.create(row, column)
            .displayName(displayName)
            .icon(iconSupplier)
            .entries(
                (displayContext, entryMap) -> {
                    entries.forEach(entryMap::add);
                    finalCollector.accept(displayContext, entryMap);
                }
            )
            .texture(this.texture);

        if ( !scrollbar )
            builder.noScrollbar();

        if ( !renderName )
            builder.noRenderedName();

        if ( special )
            builder.special();

        this.thing = builder.build();

        return this;
    }


    @Override
    public ItemGroupPreRegistrable.IconStage position(ItemGroup.Row row, int column) {
        Objects.requireNonNull(row);
        this.row = row;
        this.column = column;
        return this;
    }


    @Override
    public ItemGroupPreRegistrable.AppearanceStage icon(Supplier<ItemStack> iconSupplier) {
        Objects.requireNonNull(iconSupplier);
        this.iconSupplier = iconSupplier;
        return this;
    }

    @Override
    public ItemGroupPreRegistrable.TextureStage appearance(boolean scrollbar, boolean renderName, boolean special) {
        this.scrollbar = scrollbar;
        this.renderName = renderName;
        this.special = special;
        return this;
    }

    @Override
    public ItemGroupPreRegistrable.EntryCollectStage texture(Identifier texture) {
        Objects.requireNonNull(texture);
        this.texture = texture;
        return this;
    }

    @Override
    public ItemGroupPreRegistrable.EntryCollectStage entry(ItemStack stack, ItemGroup.StackVisibility visibility) {
        Objects.requireNonNull(stack);
        Objects.requireNonNull(visibility);
        this.entries.put(stack, visibility);
        return this;
    }


    @Override
    public ItemGroupPreRegistrable.EntryCollectStage stackEntries(Collection<ItemStack> stacks, ItemGroup.StackVisibility visibility) {
        Objects.requireNonNull(stacks);
        Objects.requireNonNull(visibility);
        stacks.stream()
            .filter(Objects::nonNull)
            .forEach(
                stack -> this.entries.put(stack, visibility)
            );

        return this;
    }

    @Override
    public ItemGroupPreRegistrable.EntryCollectStage collectBy(ItemGroup.EntryCollector collector) {
        Objects.requireNonNull(collector);
        this.entryCollector =
            ItemGroupCollectorUtils.andThen(this.entryCollector, collector);
        return this;
    }

    @Override
    public ItemGroupPreRegistrable.EntryCollectStage collectByContext(Consumer<ItemGroup.DisplayContext> action) {
        Objects.requireNonNull(action);
        this.collectorsByContext.add(action);
        return this;
    }
}
