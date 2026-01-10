package io.github.piscescup.mc.fabric.util;

import net.minecraft.item.ItemGroup;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 *
 * @author REN YuanTong
 * @since
 */
public final class ItemGroupCollectorUtils {
    private ItemGroupCollectorUtils() {}

    public static final ItemGroup.EntryCollector EMPTY_ENTRIES = (displayContext, entries) -> {};

    public static ItemGroup.EntryCollector andThen(ItemGroup.EntryCollector first, ItemGroup.EntryCollector after) {
        CheckUtil.NullCheck.requireNotNull(first);
        CheckUtil.NullCheck.requireNotNull(after);
        return (displayContext, entries) -> {
            first.accept(displayContext, entries);
            after.accept(displayContext, entries);
        };
    }

    public static ItemGroup.EntryCollector scheduleEntryCollectors(
        @NotNull Collection<ItemGroup.@NotNull EntryCollector> collectors
    ) {
        CheckUtil.NullCheck.requireAllNotNull(collectors);

        return collectors.stream()
            .filter(CheckUtil.NullCheck::nonNull)
            .reduce(EMPTY_ENTRIES, ItemGroupCollectorUtils::andThen);
    }

    public static ItemGroup.EntryCollector scheduleContextToCollectors(
        Collection<Consumer<ItemGroup.DisplayContext>> actions
    ) {
        CheckUtil.NullCheck.requireAllNotNull(actions);
        List<ItemGroup.EntryCollector> collectors = actions.stream()
            .filter(Objects::nonNull)
            .<ItemGroup.EntryCollector>map(consumer ->
                (context, entries) -> consumer.accept(context)
            )
            .toList();

        return scheduleEntryCollectors(collectors);
    }


}
