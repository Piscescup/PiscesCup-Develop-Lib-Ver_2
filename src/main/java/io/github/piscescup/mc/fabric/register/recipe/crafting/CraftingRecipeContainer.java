package io.github.piscescup.mc.fabric.register.recipe.crafting;

import io.github.piscescup.mc.fabric.util.CheckUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 *
 * @author REN YuanTong
 * @since  1.0.0
 */
public class CraftingRecipeContainer<T extends CraftingRecipe<T>> implements List<T> {
    public static final CraftingRecipeContainer<ShapedCraftingRecipe> SHAPED_RECIPES =
        create();

    public static final CraftingRecipeContainer<ShapelessCraftingRecipe> SHAPELESS_RECIPES =
        create();

    public static final CraftingRecipeContainer<ReversibleCompactingCraftingRecipe> REVERSIBLE_COMPACTING_RECIPES =
        create();

    private final List<T> recipes;

    private CraftingRecipeContainer() {
        this.recipes = new ArrayList<>();
    }

    @Contract(value = " -> new", pure = true)
    public static <T extends CraftingRecipe<T>> @NotNull CraftingRecipeContainer<T> create() {
        return new CraftingRecipeContainer<>();
    }

    @Override
    public int size() {
        return recipes.size();
    }

    @Override
    public boolean isEmpty() {
        return recipes.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        CheckUtil.NullCheck.requireNotNull(o);
        return recipes.contains(o);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return recipes.iterator();
    }

    @Override
    public @NotNull Object[] toArray() {
        return recipes.toArray();
    }

    @Override
    public @NotNull <R> R[] toArray(@NotNull R[] a) {
        return recipes.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return recipes.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return recipes.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return recipes.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return recipes.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        return recipes.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return recipes.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return recipes.retainAll(c);
    }

    @Override
    public void clear() {
        recipes.clear();
    }

    @Override
    public T get(int index) {
        return recipes.get(index);
    }

    @Override
    public T set(int index, T element) {
        return recipes.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        recipes.add(index, element);
    }

    @Override
    public T remove(int index) {
        return recipes.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return recipes.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return recipes.lastIndexOf(o);
    }

    @Override
    public @NotNull ListIterator<T> listIterator() {
        return recipes.listIterator();
    }

    @Override
    public @NotNull ListIterator<T> listIterator(int index) {
        return recipes.listIterator(index);
    }

    @Override
    public @NotNull List<T> subList(int fromIndex, int toIndex) {
        return recipes.subList(fromIndex, toIndex);
    }
}
