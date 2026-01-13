package io.github.piscescup.mc.fabric.register.itemgroup;

import io.github.piscescup.mc.fabric.register.Register;
import io.github.piscescup.mc.fabric.utils.CheckUtils.NullCheck;
import io.github.piscescup.mc.fabric.utils.ItemGroupCollectorUtils;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.github.piscescup.mc.fabric.utils.ItemGroupCollectorUtils.ENTRY_COLLECTOR;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public class ItemGroupRegister
    extends Register<ItemGroup, ItemGroupPostRegistrable, ItemGroupRegister>
    implements ItemGroupPreRegistrable.PositionStage,
                ItemGroupPreRegistrable.IconStage, ItemGroupPreRegistrable.AppearanceStage,
                ItemGroupPreRegistrable.EntryCollectStage, ItemGroupPreRegistrable.TextureStage,
                ItemGroupPostRegistrable
{

    private ItemGroup.Row row;
    private int column;
    private Text displayName = Text.empty();
    private Supplier<ItemStack> iconSupplier = () -> ItemStack.EMPTY;
    private boolean scrollbar;
    private boolean renderName;
    private boolean special;
    private ItemGroup.Type type = ItemGroup.Type.CATEGORY;
    private Identifier texture;

    private ItemGroup.EntryCollector entryCollector = ENTRY_COLLECTOR;
    private final Map<ItemStack, ItemGroup.StackVisibility> entries;
    private final List<Consumer<ItemGroup.DisplayContext>> entryContextCollectors;

    private ItemGroupRegister(Identifier id) {
        super(RegistryKeys.ITEM_GROUP, id);
        entryContextCollectors = new ArrayList<>();
        entries = new LinkedHashMap<>();
    }

    @Contract("_ -> new")
    public static ItemGroupPreRegistrable.@NotNull PositionStage createFor(Identifier id) {
        return new ItemGroupRegister(id);
    }

    @Contract("_, _ -> new")
    public static ItemGroupPreRegistrable.@NotNull PositionStage createFor(String namespace, String path) {
        return createFor(Identifier.of(namespace, path) );
    }

    @Override
    public ItemGroupPostRegistrable register() {
        String translationKey = Util.createTranslationKey("itemGroups", this.id);

        ItemGroup.Builder builder = ItemGroup.create(this.row, this.column)
            .displayName(Text.translatable(translationKey))
            .icon(this.iconSupplier)
            .texture(this.texture);

        if (this.special) builder.special();
        if (!this.scrollbar) builder.noScrollbar();
        if (!this.renderName) builder.noRenderedName();

        ItemGroup.EntryCollector collectorByContext =
            ItemGroupCollectorUtils.scheduleContextToCollectors(this.entryContextCollectors);

        ItemGroup.EntryCollector finalCollector =
            ItemGroupCollectorUtils.andThen(this.entryCollector, collectorByContext);

        ItemGroup group = builder.entries((displayContext, entries) -> {
                this.entries.forEach(entries::add);
                finalCollector.accept(displayContext, entries);
            }).build();

        this.thing = Registry.register(
            Registries.ITEM_GROUP,
            this.id,
            group
        );

        return this;
    }

    @Override
    public ItemGroupPreRegistrable.TextureStage appearance(boolean scrollbar, boolean renderDisplayName, boolean specialItemGroup) {
        this.scrollbar = scrollbar;
        this.renderName = renderDisplayName;
        this.special = specialItemGroup;
        return this;
    }

    @Override
    public ItemGroupPreRegistrable.IconStage position(ItemGroup.Row row, int column) {
        this.row = row;
        this.column = column;
        return this;
    }

    @Override
    public ItemGroupPreRegistrable.AppearanceStage icon(@NotNull Supplier<ItemStack> iconSupplier) {
        NullCheck.requireNonNull(iconSupplier);
        this.iconSupplier = iconSupplier;
        return this;
    }


    @Override
    public ItemGroupPreRegistrable.EntryCollectStage addStackEntry(@NotNull ItemStack stack, ItemGroup.StackVisibility visibility) {
        this.entries.put(stack, visibility);
        return this;
    }

    @Override
    public ItemGroupPreRegistrable.EntryCollectStage addStackEntries(@NotNull Collection<ItemStack> items, ItemGroup.StackVisibility visibility) {
        items.forEach(
            stack -> this.entries.put(stack, visibility)
        );
        return this;
    }

    @Override
    public ItemGroupPreRegistrable.EntryCollectStage collectBy(ItemGroup.@NotNull EntryCollector collector) {
        NullCheck.requireNonNull(collector);
        this.entryCollector = ItemGroupCollectorUtils.andThen(this.entryCollector, collector);
        return this;
    }

    @Override
    public ItemGroupPreRegistrable.EntryCollectStage collectByContext(@NotNull Consumer<ItemGroup.DisplayContext> action) {
        NullCheck.requireNonNull(action);
        this.entryContextCollectors.add(action);
        return this;
    }

    @Override
    public ItemGroupPreRegistrable.EntryCollectStage texture(@NotNull Identifier texture) {
        NullCheck.requireNonNull(texture);
        this.texture = texture;
        return this;
    }
}
