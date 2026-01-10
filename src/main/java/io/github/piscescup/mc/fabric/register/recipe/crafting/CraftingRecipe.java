package io.github.piscescup.mc.fabric.register.recipe.crafting;

import io.github.piscescup.mc.fabric.utils.CheckUtils.NullCheck;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.item.ItemConvertible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public abstract sealed class CraftingRecipe<R extends CraftingRecipe<R>>
    permits ShapedCraftingRecipe, ShapelessCraftingRecipe, ReversibleCompactingCraftingRecipe
{
    protected ItemConvertible output;
    protected int outputCount;

    @Nullable
    protected String group;
    protected final Map<String, AdvancementCriterion<?>> criteria;


    protected CraftingRecipe(ItemConvertible output, int outputCount) {
        this.output = output;
        this.outputCount = outputCount;
        this.criteria = new LinkedHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public R group(@Nullable String group) {
        this.group = group;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R criteria(@NotNull String name, @NotNull AdvancementCriterion<?> criterion) {
        NullCheck.requireNonNull(name);
        NullCheck.requireNonNull(criterion);
        this.criteria.put(name, criterion);
        return (R) this;
    }

    public ItemConvertible getOutputItem() {
        return output;
    }

    public int getOutputCount() {
        return outputCount;
    }

    public @Nullable String getGroup() {
        return group;
    }

    public Map<String, AdvancementCriterion<?>> getCriteria() {
        return criteria;
    }

    @SuppressWarnings("unchecked")
    public R collectTo(@NotNull CraftingRecipeContainer<R> container) {
        NullCheck.requireNonNull(container);
        container.add((R) this);
        return (R) this;
    }
}
