package io.github.piscescup.mc.fabric.utils;

import net.minecraft.item.ItemGroup;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 *
 * @author REN YuanTong
 * @Date 2026-01-09
 * @since 1.0.0
 */
public final class ItemGroupCollectorUtils {
    private ItemGroupCollectorUtils() {}

    /**
     * An empty entry collector that does nothing.
     */
    public static final ItemGroup.EntryCollector ENTRY_COLLECTOR =
        (displayContext, entries) -> {};

    /**
     * Compose two entry collectors into one.
     * @param first The first entry collector to be executed.
     * @param after The second entry collector to be executed after the first.
     * @return A composed entry collector that performs, in sequence, the operations of the first
     *         followed by the operations of the after.
     */
    public static ItemGroup.EntryCollector andThen(
        ItemGroup.EntryCollector first,
        ItemGroup.EntryCollector after
    ) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(after);

        return (enabledFeatures, entries) -> {
            first.accept(enabledFeatures, entries);
            after.accept(enabledFeatures, entries);
        };
    }

    /**
     * Schedule multiple entry collectors into one.
     * @param collectors The list of entry collectors to be scheduled.
     * @return A scheduled entry collector that performs, in sequence, the operations of all
     *         collectors in the provided list.
     */
    public static ItemGroup.EntryCollector scheduleEntryCollector(
        Collection<ItemGroup.EntryCollector> collectors
    ) {
        Objects.requireNonNull(collectors);

        return collectors.stream()
            .filter(Objects::nonNull)
            .reduce(ENTRY_COLLECTOR, ItemGroupCollectorUtils::andThen);
    }

    /**
     * Schedule multiple context actions (Presented as {@code Consumer}) into one entry collector.
     * @param contextActions The list of context actions to be scheduled.
     * @return A scheduled entry collector that performs, in sequence, the operations of all
     */
    public static ItemGroup.EntryCollector scheduleContextCollector(
        Collection<Consumer<ItemGroup.DisplayContext>> contextActions
    ) {
        Objects.requireNonNull(contextActions);

        List<ItemGroup.EntryCollector> collectors = contextActions.stream()
            .filter(Objects::nonNull)
            .<ItemGroup.EntryCollector>map(consumer ->
                (context, entries) -> consumer.accept(context)
            )
            .toList();

        return scheduleEntryCollector(collectors);
    }
}
