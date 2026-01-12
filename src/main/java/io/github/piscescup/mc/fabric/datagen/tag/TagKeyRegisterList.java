package io.github.piscescup.mc.fabric.datagen.tag;

import io.github.piscescup.mc.fabric.register.tag.TagKeyRegister;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 *
 * @author REN YuanTong
 * @since
 */
public class TagKeyRegisterList<T>
    extends AbstractList<TagKeyRegister<T>>
    implements List<TagKeyRegister<T>>
{
    private final List<TagKeyRegister<T>> tagKeyRegisters;

    TagKeyRegisterList() {
        this.tagKeyRegisters = new ArrayList<>();
    }

    @Override
    public int size() {
        return this.tagKeyRegisters.size();
    }

    @Override
    public boolean isEmpty() {
        return this.tagKeyRegisters.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.tagKeyRegisters.contains(o);
    }

    @Override
    public @NotNull Iterator<TagKeyRegister<T>> iterator() {
        return this.tagKeyRegisters.iterator();
    }

    @Override
    public @NotNull Object[] toArray() {
        return this.tagKeyRegisters.toArray();
    }

    @Override
    public @NotNull <T> T[] toArray(@NotNull T[] a) {
        return this.tagKeyRegisters.toArray(a);
    }

    @Override
    public boolean add(TagKeyRegister<T> tagKeyRegister) {
        return this.tagKeyRegisters.add(tagKeyRegister);
    }

    @Override
    public boolean remove(Object o) {
        return this.tagKeyRegisters.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return this.tagKeyRegisters.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends TagKeyRegister<T>> c) {
        return this.tagKeyRegisters.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends TagKeyRegister<T>> c) {
        return this.tagKeyRegisters.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return this.tagKeyRegisters.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return this.tagKeyRegisters.retainAll(c);
    }

    @Override
    public void clear() {
        this.tagKeyRegisters.clear();
    }

    @Override
    public TagKeyRegister<T> get(int index) {
        return this.tagKeyRegisters.get(index);
    }

    @Override
    public TagKeyRegister<T> set(int index, TagKeyRegister<T> element) {
        return this.tagKeyRegisters.set(index, element);
    }

    @Override
    public void add(int index, TagKeyRegister<T> element) {
        this.tagKeyRegisters.add(index, element);
    }

    @Override
    public TagKeyRegister<T> remove(int index) {
        return this.tagKeyRegisters.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.tagKeyRegisters.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.tagKeyRegisters.lastIndexOf(o);
    }

    @Override
    public @NotNull ListIterator<TagKeyRegister<T>> listIterator() {
        return this.tagKeyRegisters.listIterator();
    }

    @Override
    public @NotNull ListIterator<TagKeyRegister<T>> listIterator(int index) {
        return this.tagKeyRegisters.listIterator(index);
    }

    @Override
    public @NotNull List<TagKeyRegister<T>> subList(int fromIndexInclusive, int toIndexExclusive) {
        return this.tagKeyRegisters.subList(fromIndexInclusive, toIndexExclusive);
    }


}