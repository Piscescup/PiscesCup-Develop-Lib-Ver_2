package io.github.piscescup.mc.fabric.register.recipe.crafting;

import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class ReversibleCompactingCraftingRecipe
    extends CraftingRecipe<ReversibleCompactingCraftingRecipe>
{
    private RecipeCategory reverseCategory;
    private ItemConvertible baseItem;
    private RecipeCategory compactingCategory;
    private String compactingId;
    private @Nullable String compactingGroup = null;
    private String reverseId;
    private @Nullable String reverseGroup = null;

    private CompactingType compactingType;

    private ReversibleCompactingCraftingRecipe(
        RecipeCategory reverseCategory,
        ItemConvertible baseItem,
        RecipeCategory compactingCategory,
        ItemConvertible compactingItem,
        CompactingType compactingType
    ) {
        int count = compactingType.count();
        super(compactingItem, count);
        this.reverseCategory = reverseCategory;
        this.compactingCategory = compactingCategory;
        this.baseItem = baseItem;
        this.compactingId = getRecipeName(compactingItem);
        this.reverseId = getRecipeName(baseItem);
        this.compactingType = compactingType;
    }

    public ReversibleCompactingCraftingRecipe compactingGroup(@Nullable String compactingGroup) {
        this.compactingGroup = group;
        return this;
    }

    public ReversibleCompactingCraftingRecipe reverseGroup(@Nullable String reverseGroup) {
        this.reverseGroup = group;
        return this;
    }

    public ReversibleCompactingCraftingRecipe compactingId(@Nullable String compactingId) {
        this.compactingId = compactingId;
        return this;
    }

    public ReversibleCompactingCraftingRecipe reverseId(@Nullable String reverseId) {
        this.reverseId = reverseId;
        return this;
    }

    public RecipeCategory getReverseCategory() {
        return reverseCategory;
    }

    public ItemConvertible getBaseItem() {
        return baseItem;
    }

    public RecipeCategory getCompactingCategory() {
        return compactingCategory;
    }

    public String getCompactingId() {
        return compactingId;
    }

    public @Nullable String getCompactingGroup() {
        return compactingGroup;
    }

    public String getReverseId() {
        return reverseId;
    }

    public @Nullable String getReverseGroup() {
        return reverseGroup;
    }

    public CompactingType getCompactingType() {
        return compactingType;
    }

    public static String getItemPath(ItemConvertible item) {
        return Registries.ITEM.getId(item.asItem()).getPath();
    }

    public static String getRecipeName(ItemConvertible item) {
        return getItemPath(item);
    }

    public static
    enum CompactingType {
        REVERSIBLE_2x2(
            generatePattern(2, 2)
        ),

        REVERSIBLE_2x3(
            generatePattern(2, 3)
        ),

        REVERSIBLE_3x2(
            generatePattern(3, 2)
        ),

        REVERSIBLE_3x3(
            generatePattern(3, 3)
        ),

        ;

        public static final char SYMBOL = '#';

        private final List<String> pattern;
        CompactingType(List<String> pattern) {
            this.pattern = pattern;
        }

        private static List<String> generatePattern(int row, int column) {
            String singleRowString = String.valueOf(SYMBOL).repeat(column);
            List<String> pattern = new ArrayList<>();
            for (int i = 0; i < row; i++) {
                pattern.add(singleRowString);
            }
            return Collections.unmodifiableList(pattern);
        }

        @Override
        public String toString() {
            return String.join("\n", pattern);
        }

        public List<String> getPattern() {
            return pattern;
        }

        public int count() {
            return pattern.stream()
                .mapToInt(String::length)
                .sum();
        }
    }
}
