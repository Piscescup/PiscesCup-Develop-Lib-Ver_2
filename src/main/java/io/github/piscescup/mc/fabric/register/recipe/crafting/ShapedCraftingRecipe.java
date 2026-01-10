package io.github.piscescup.mc.fabric.register.recipe.crafting;

import io.github.piscescup.mc.fabric.utils.CheckUtils.NullCheck;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 *
 * @author REN YuanTong
 * @since
 */
public final class ShapedCraftingRecipe extends CraftingRecipe<ShapedCraftingRecipe> {
    private final List<String> patterns;
    private final Map<Character, Ingredient> ingredientDefinitions;
    private final Map<Character, TagKey<Item>> tagDefinitions;

    private ShapedCraftingRecipe(ItemConvertible output, int outputCount) {
        super(output, outputCount);
        this.patterns = new ArrayList<>();
        this.ingredientDefinitions = new LinkedHashMap<>();
        this.tagDefinitions = new LinkedHashMap<>();
    }

    private void validatePatternOrThrow(String pattern) {
        // If the pattern is empty, the pattern is valid.
        if (this.patterns.isEmpty()) {
            return;
        }

        // Below is the case that the pattern is not empty.
        int rowLength = this.patterns.getFirst().length();
        int patternLength = pattern.length();

        if ( patternLength == 0 || patternLength != rowLength ) {
            throw new IllegalArgumentException(
                "The pattern: (%s) has invalid pattern length: (%d), expected: (%d)."
                    .formatted(pattern,  patternLength, rowLength)
            );
        }
    }

    public ShapedCraftingRecipe pattern(@NotNull String pattern) {
        NullCheck.requireNonNull(pattern);
        validatePatternOrThrow(pattern);
        this.patterns.add(pattern);
        return this;
    }

    public ShapedCraftingRecipe definition(@NotNull Map<Character, Ingredient> definitions) {
        NullCheck.requireNonNull(definitions);
        NullCheck.requireAllNonNull(definitions.keySet());
        NullCheck.requireAllNonNull(definitions.values());
        this.ingredientDefinitions.putAll(definitions);
        return this;
    }

    public ShapedCraftingRecipe definition(@NotNull Character symbol, Ingredient ingredient) {
        NullCheck.requireNonNull(symbol);
        NullCheck.requireNonNull(ingredient);
        this.ingredientDefinitions.put(symbol, ingredient);
        return this;
    }

    public ShapedCraftingRecipe definition(@NotNull Character symbol, ItemConvertible item) {
        NullCheck.requireNonNull(symbol);
        NullCheck.requireNonNull(item);
        this.ingredientDefinitions.put(symbol, Ingredient.ofItem(item));
        return this;
    }

    public ShapedCraftingRecipe definition(@NotNull Character symbol, TagKey<Item> item) {
        NullCheck.requireNonNull(symbol);
        NullCheck.requireNonNull(item);
        this.tagDefinitions.put(symbol, item);
        return this;
    }

    public List<String> getPatterns() {
        return patterns;
    }

    public Map<Character, Ingredient> getIngredientDefinitions() {
        return ingredientDefinitions;
    }

    public Map<Character, TagKey<Item>> getTagDefinitions() {
        return tagDefinitions;
    }

}
