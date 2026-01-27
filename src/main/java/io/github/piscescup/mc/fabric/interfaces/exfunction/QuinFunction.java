package io.github.piscescup.mc.fabric.interfaces.exfunction;

import io.github.piscescup.mc.fabric.interfaces.Memorized;
import io.github.piscescup.mc.fabric.interfaces.entries.QuinEntry;
import io.github.piscescup.mc.fabric.utils.CheckUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Represents a function that accepts five arguments and produces a result.
 * This is the five-arity specialization of {@link Function}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object, Object, Object, Object, Object)}.
 *
 * @param <X1> the type of the first argument to the function
 * @param <X2> the type of the second argument to the function
 * @param <X3> the type of the third argument to the function
 * @param <X4> the type of the fourth argument to the function
 * @param <X5> the type of the fifth argument to the function
 * @param <Y>  the type of the result of the function
 *
 * @author REN Yuantong
 * @since 1.0.0
 */
@FunctionalInterface
public interface QuinFunction<X1, X2, X3, X4, X5, Y> {

    /**
     * Applies this function to the given arguments.
     *
     * @param x1 the first function argument
     * @param x2 the second function argument
     * @param x3 the third function argument
     * @param x4 the fourth function argument
     * @param x5 the fifth function argument
     * @return the function result
     */
    Y apply(X1 x1, X2 x2, X3 x3, X4 x4, X5 x5);

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     *
     * @param <V> the type of output of the {@code after} function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if {@code after} is {@code null}
     */
    default <V> QuinFunction<X1, X2, X3, X4, X5, V> andThen(Function<? super Y, ? extends V> after) {
        CheckUtils.NullCheck.requireNonNull(after);
        return (X1 x1, X2 x2, X3 x3, X4 x4, X5 x5) -> after.apply(apply(x1, x2, x3, x4, x5));
    }

    /**
     * Transforms this multi-argument function into a sequence of single-argument functions.
     *
     * @return the curried version of this function
     */
    default Function<X1, Function<X2, Function<X3, Function<X4, Function<X5, Y>>>>> curried() {
        return x1 -> x2 -> x3 -> x4 -> x5 -> apply(x1, x2, x3, x4, x5);
    }

    /**
     * Checks if this function is an instance of the {@link Memorized} interface.
     *
     * @return {@code true} if this function is memorized, {@code false} otherwise
     */
    default boolean isMemorized() {
        return this instanceof Memorized;
    }

    /**
     * Returns a memorized version of this function.
     *
     * @return a memorized version of this {@code QuinFunction}
     */
    default QuinFunction<X1, X2, X3, X4, X5, Y> memorized() {
        if (isMemorized()) return this;

        final Map<QuinEntry<X1, X2, X3, X4, X5>, Y> cache = new ConcurrentHashMap<>();

        return (QuinFunction<X1, X2, X3, X4, X5, Y> & Memorized) (x1, x2, x3, x4, x5) -> {
            QuinEntry<X1, X2, X3, X4, X5> entry = new QuinEntry<>(x1, x2, x3, x4, x5);
            return cache.computeIfAbsent(entry, e -> this.apply(e.x1(), e.x2(), e.x3(), e.x4(), e.x5()));
        };
    }

    /**
     * Returns a new {@code QuinFunction} where the order of the arguments is reversed.
     *
     * @return a new {@code QuinFunction} with reversed argument order
     */
    default QuinFunction<X5, X4, X3, X2, X1, Y> reversed() {
        return (X5 x5, X4 x4, X3 x3, X2 x2, X1 x1) -> apply(x1, x2, x3, x4, x5);
    }

    /**
     * Returns a {@code QuinFunction} that always returns the given constant value.
     *
     * @param constant the constant value to be returned
     * @return a {@code QuinFunction} that always returns the specified constant
     */
    static <X1, X2, X3, X4, X5, Y> QuinFunction<X1, X2, X3, X4, X5, Y> constant(Y constant) {
        return (x1, x2, x3, x4, x5) -> constant;
    }

    /**
     * Returns a {@code QuinFunction} that is a reference to the provided function.
     *
     * @param pentaFunc the quin-function to be returned; must not be {@code null}
     * @return the non-null quin-function
     * @throws NullPointerException if the provided function is {@code null}
     */
    static <X1, X2, X3, X4, X5, Y> QuinFunction<X1, X2, X3, X4, X5, Y> of(QuinFunction<X1, X2, X3, X4, X5, Y> pentaFunc) {
        CheckUtils.NullCheck.requireNonNull(pentaFunc);
        return pentaFunc;
    }

    /**
     * Narrows the given quin-function to a specific type.
     *
     * @param pentaFunc the quin-function to narrow
     * @return a narrowed quin-function
     * @throws NullPointerException if the provided function is {@code null}
     */
    @SuppressWarnings("unchecked")
    static <X1, X2, X3, X4, X5, Y> QuinFunction<X1, X2, X3, X4, X5, Y> narrow(QuinFunction<? super X1, ? super X2, ? super X3, ? super X4, ? super X5, ? super Y> pentaFunc) {
        CheckUtils.NullCheck.requireNonNull(pentaFunc);
        return (QuinFunction<X1, X2, X3, X4, X5, Y>) pentaFunc;
    }
}