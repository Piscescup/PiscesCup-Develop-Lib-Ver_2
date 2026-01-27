package io.github.piscescup.mc.fabric.interfaces.entries;

import java.util.List;

/**
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
