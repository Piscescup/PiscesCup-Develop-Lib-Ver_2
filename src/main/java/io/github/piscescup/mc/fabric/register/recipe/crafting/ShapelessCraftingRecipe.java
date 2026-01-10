package io.github.piscescup.mc.fabric.register.recipe.crafting;

import io.github.piscescup.mc.fabric.utils.CheckUtils.NullCheck;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class ShapelessCraftingRecipe extends CraftingRecipe<ShapelessCraftingRecipe> {
    private List<Ingredient> ingredients;
    private List<TagKey<Item>> tags;

    private ShapelessCraftingRecipe(ItemConvertible output, int outputCount) {
        super(output, outputCount);
        this.ingredients = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    @Contract("_, _ -> new")
    public static @NotNull ShapelessCraftingRecipe createFor(ItemConvertible output, int outputCount) {
        return new ShapelessCraftingRecipe(output, outputCount);
    }

    @Contract("_ -> new")
    public static @NotNull ShapelessCraftingRecipe createFor(ItemConvertible output) {
        return new ShapelessCraftingRecipe(output, 1);
    }

    public ShapelessCraftingRecipe input(@NotNull Ingredient ingredient) {
        NullCheck.requireNonNull(ingredient);
        return this.input(ingredient, 1);
    }

    public ShapelessCraftingRecipe input(@NotNull Ingredient ingredient, int count) {
        NullCheck.requireNonNull(ingredient);
        for (int i = 0; i < count; i++) {
            this.ingredients.add(ingredient);
        }
        return this;
    }

    public ShapelessCraftingRecipe input(@NotNull ItemConvertible ingredient) {
        NullCheck.requireNonNull(ingredient);
        return this.input(ingredient, 1);
    }

    public ShapelessCraftingRecipe input(@NotNull ItemConvertible item, int count) {
        NullCheck.requireNonNull(item);
        for (int i = 0; i < count; i++) {
            this.ingredients.add(Ingredient.ofItem(item));
        }
        return this;
    }

    public ShapelessCraftingRecipe input(@NotNull TagKey<Item> tag) {
        NullCheck.requireNonNull(tag);
        this.tags.add(tag);
        return this;
    }






}
