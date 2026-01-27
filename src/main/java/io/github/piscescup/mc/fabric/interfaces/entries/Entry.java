package io.github.piscescup.mc.fabric.interfaces.entries;

import java.util.List;

/**
 * The {@code Entry} interface defines a contract for an entry that can hold multiple elements.
 * It provides methods to get the number of elements in the entry and to convert the entry into a List.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public interface Entry {
    /**
     * Returns the number of elements of this entry.
     *
     * @return the number of elements.
     */
    int arity();

    /**
     * Converts the current entry into a list containing its elements.
     *
     * @return a List containing the elements of this entry.
     */
    List<?> toList();
}
