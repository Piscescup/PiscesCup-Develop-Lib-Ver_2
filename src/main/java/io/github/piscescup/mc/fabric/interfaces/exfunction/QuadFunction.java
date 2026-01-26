package io.github.piscescup.mc.fabric.interfaces.exfunction;

import io.github.piscescup.mc.fabric.interfaces.Memorized;
import io.github.piscescup.mc.fabric.interfaces.entries.QuadEntry;
import io.github.piscescup.mc.fabric.utils.CheckUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Represents a function that accepts four arguments and produces a result.
 * This is the four-arity specialization of {@link Function}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object, Object, Object, Object)}.
 *
 * @param <X1> the type of the first argument to the function
 * @param <X2> the type of the second argument to the function
 * @param <X3> the type of the third argument to the function
 * @param <X4> the type of the fourth argument to the function
 * @param <Y> the type of the result of the function
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
@FunctionalInterface
public interface QuadFunction<X1, X2, X3, X4, Y> {

    /**
     * Applies this function to the given arguments.
     *
     * @param x1 the first function argument
     * @param x2 the second function argument
     * @param x3 the third function argument
     * @param x4 the fourth function argument
     * @return the function result
     */
    Y apply(X1 x1, X2 x2, X3 x3, X4 x4);

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
    default <V> QuadFunction<X1, X2, X3, X4, V> andThen(Function<? super Y, ? extends V> after) {
        CheckUtils.NullCheck.requireNonNull(after);
        return (X1 x1, X2 x2, X3 x3, X4 x4) -> after.apply(apply(x1, x2, x3, x4));
    }

    /**
     * Transforms this multi-argument function into a sequence of single-argument functions.
     *
     * @return the curried version of this function
     */
    default Function<X1, Function<X2, Function<X3, Function<X4, Y>>>> curried() {
        return x1 -> x2 -> x3 -> x4 -> apply(x1, x2, x3, x4);
    }

    /**
     * Checks if this function is an instance of the {@link Memorized} interface,
     * indicating that it has been memorized and can potentially cache results for
     * repeated input arguments.
     *
     * @return {@code true} if this function is memorized, {@code false} otherwise
     */
    default boolean isMemorized() {
        return this instanceof Memorized;
    }

    /**
     * Returns a memorized version of this function. If the function is already
     * memorized, it returns the current instance. Otherwise, it creates a new
     * function that caches results for the given input arguments to avoid redundant
     * computations.
     *
     * @return a memorized version of this {@code QuadFunction}
     */
    default QuadFunction<X1, X2, X3, X4, Y> memorized() {
        if (isMemorized()) return this;

        final Map<QuadEntry<X1, X2, X3, X4>, Y> cache = new ConcurrentHashMap<>();

        return (QuadFunction<X1, X2, X3, X4, Y> & Memorized) (x1, x2, x3, x4) -> {
            QuadEntry<X1, X2, X3, X4> entry = new QuadEntry<>(x1, x2, x3, x4);
            return cache.computeIfAbsent(entry, e -> this.apply(e.x1(), e.x2(), e.x3(), e.x4()));
        };
    }

    /**
     * Returns a new {@code QuadFunction} where the order of the arguments is reversed.
     * The returned function will accept its arguments in the order (x1, x2, x3, x4) and pass them
     * to the original function in the reversed order (x4, x3, x2, x1).
     *
     * @return a new {@code QuadFunction} that takes four arguments in the reverse order
     *         compared to this function and then applies them to this function
     */
    default QuadFunction<X4, X3, X2, X1, Y> reversed() {
        return (X4 x4, X3 x3, X2 x2, X1 x1) -> apply(x1, x2, x3, x4);
    }

    /**
     * Returns a {@code QuadFunction} that always returns the given constant value, regardless of the input arguments.
     *
     * @param <X1> the type of the first argument to the function
     * @param <X2> the type of the second argument to the function
     * @param <X3> the type of the third argument to the function
     * @param <X4> the type of the fourth argument to the function
     * @param <Y>  the type of the result of the function
     * @param constant the constant value to be returned by the function
     * @return a {@code QuadFunction} that always returns the specified constant value
     */
    static <X1, X2, X3, X4, Y> QuadFunction<X1, X2, X3, X4, Y> constant(Y constant) {
        return (x1, x2, x3, x4) -> constant;
    }

    /**
     * Returns a {@code QuadFunction} that is a reference to the provided function, ensuring it is not {@code null}.
     *
     * @param <X1> the type of the first argument to the function
     * @param <X2> the type of the second argument to the function
     * @param <X3> the type of the third argument to the function
     * @param <X4> the type of the fourth argument to the function
     * @param <Y>  the type of the result of the function
     * @param quadFunc the quad function to be returned; must not be {@code null}
     * @return the non-null quad function
     * @throws NullPointerException if the provided quad function is {@code null}
     */
    static <X1, X2, X3, X4, Y> QuadFunction<X1, X2, X3, X4, Y> of(QuadFunction<X1, X2, X3, X4, Y> quadFunc) {
        CheckUtils.NullCheck.requireNonNull(quadFunc);
        return quadFunc;
    }

    /**
     * Narrows the given quad function to a specific type, ensuring that the provided function is not null.
     *
     * @param <X1> the type of the first argument to the function
     * @param <X2> the type of the second argument to the function
     * @param <X3> the type of the third argument to the function
     * @param <X4> the type of the fourth argument to the function
     * @param <Y>  the type of the result of the function
     * @param quadFunc the quad function to narrow
     * @return a narrowed quad function
     * @throws NullPointerException if the provided quad function is {@code null}
     */
    @SuppressWarnings("unchecked")
    static <X1, X2, X3, X4, Y> QuadFunction<X1, X2, X3, X4, Y> narrow(QuadFunction<? super X1, ? super X2, ? super X3, ? super X4, ? super Y> quadFunc) {
        CheckUtils.NullCheck.requireNonNull(quadFunc);
        return (QuadFunction<X1, X2, X3, X4, Y>) quadFunc;
    }

}