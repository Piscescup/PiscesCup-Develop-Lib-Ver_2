package io.github.piscescup.mc.fabric.interfaces.entries;

import java.util.List;

/**
 * Represents a 4 element entry.
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public record QuadEntry<X1, X2, X3, X4>(X1 x1, X2 x2, X3 x3, X4 x4) implements Entry {

    @Override
    public int arity() {
        return 4;
    }

    @Override
    public List<?> toList() {
        return List.of(x1, x2, x3, x4);
    }
}
