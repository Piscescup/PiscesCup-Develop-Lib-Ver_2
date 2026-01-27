package io.github.piscescup.mc.fabric.interfaces.entries;

import java.util.List;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public record TriEntry<X1, X2, X3>(X1 x1, X2 x2, X3 x3) implements Entry {

    @Override
    public int arity() {
        return 3;
    }

    @Override
    public List<?> toList() {
        return List.of(x1, x2, x3);
    }
}
