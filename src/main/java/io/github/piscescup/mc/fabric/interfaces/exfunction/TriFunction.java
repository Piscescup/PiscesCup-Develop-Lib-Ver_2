package io.github.piscescup.mc.fabric.interfaces.exfunction;

import io.github.piscescup.mc.fabric.interfaces.Memorized;
import io.github.piscescup.mc.fabric.interfaces.entries.TriEntry;
import io.github.piscescup.mc.fabric.utils.CheckUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


/**
 * Represents a function that accepts three arguments and produces a result.
 * This is the three-arity specialization of {@link Function}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object, Object, Object)}.
 *
 * @param <X1> the type of the first argument to the function
 * @param <X2> the type of the second argument to the function
 * @param <X3> the type of the third argument to the function
 * @param <Y> the type of the result of the function
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
@FunctionalInterface
public interface TriFunction<X1, X2, X3, Y> {

    /**
     * Applies this function to the given arguments.
     *
     * @param x1 the first function argument
     * @param x2 the second function argument
     * @param x3 the third function argument
     * @return the function result
     */
    Y apply(X1 x1, X2 x2, X3 x3);

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V> the type of output of the {@code after} function, and of the
     * composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if {@code after} is {@code null}
     */
    default <V> TriFunction<X1, X2, X3, V> andThen(Function<? super Y, ? extends V> after) {
        CheckUtils.NullCheck.requireNonNull(after);
        return (X1 x1, X2 x2, X3 x3) -> after.apply(apply(x1, x2, x3));
    }

    /**
     * Returns a curried version of this function. The returned function takes one argument and returns
     * another function that takes the next argument, and so on, until all arguments have been supplied,
     * at which point the result is computed.
     *
     * @return a function that accepts one argument and returns a function that accepts the next argument,
     *         eventually returning the result of applying this function to its arguments
     */
    default Function<X1, Function<X2, Function<X3, Y>>> curried() {
        return (X1 x1) -> (X2 x2) -> (X3 x3) -> this.apply(x1, x2, x3);
    }

    /**
     * Checks if this function is an instance of the {@link Memorized} interface, indicating that
     * it has been memorized and can potentially return cached results for previously computed inputs.
     *
     * @return {@code true} if this function is an instance of {@link Memorized}, {@code false} otherwise
     */
    default boolean isMemorized() {
        return this instanceof Memorized;
    }

    /**
     * Returns a memorized version of this function. If the function is already
     * memorized, it returns the current instance. Otherwise, it creates a new
     * function that caches the results of previous invocations using a
     * {@link ConcurrentHashMap} to store the computed values.
     *
     * @return a memorized version of this function
     */
    default TriFunction<X1, X2, X3, Y> memorized() {
        if (isMemorized()) return this;
        final Map<TriEntry<X1, X2, X3>, Y> cache = new ConcurrentHashMap<>();
        return (TriFunction<X1, X2, X3, Y> & Memorized) (X1 x1, X2  x2, X3 x3) -> {
            TriEntry<X1, X2, X3> triEntry = new TriEntry<>(x1, x2, x3);
            return cache.computeIfAbsent(triEntry, entry -> this.apply(entry.x1(), entry.x2(), entry.x3()));
        };
    }

    /**
     * Returns a new {@code TriFunction} with the order of the first and third arguments reversed.
     * The returned function will accept three arguments in the order (x3, x2, x1) and
     * apply them to this function as (x1, x2, x3).
     *
     * @return a new {@code TriFunction} with the argument order reversed
     */
    default TriFunction<X3, X2, X1, Y> reversed() {
        return (x3, x2, x1) -> this.apply(x1, x2, x3);
    }

    /**
     * Returns a function which always returns the given constant, regardless of the input arguments.
     *
     * @param <X1> the type of the first argument to the function
     * @param <X2> the type of the second argument to the function
     * @param <X3> the type of the third argument to the function
     * @param <R> the type of the result of the function
     * @param constant the value to be returned by the resulting function
     * @return a function that accepts three arguments and always returns the specified constant
     * @throws NullPointerException if {@code constant} is {@code null}
     */
    static <X1, X2, X3, R> TriFunction<X1, X2, X3, R> constant(R constant) {
        CheckUtils.NullCheck.requireNonNull(constant);
        return (X1 x1, X2 x2, X3 x3) -> constant;
    }

    /**
     * Returns the given tri-function directly.
     *
     * @param <X1> the type of the first argument to the function
     * @param <X2> the type of the second argument to the function
     * @param <X3> the type of the third argument to the function
     * @param <R>  the result type of the function
     * @param triFunc the tri-function to return
     * @return the same tri-function that was passed in
     * @throws NullPointerException if {@code triFunc} is {@code null}
     */
    static <X1, X2, X3, R> TriFunction<X1, X2, X3, R> of(TriFunction<X1, X2, X3, R> triFunc) {
        CheckUtils.NullCheck.requireNonNull(triFunc);
        return triFunc;
    }

    /**
     * Narrows the type of given TriFunction to the specified types.
     *
     * @param <X1> the type of the first argument to the function
     * @param <X2> the type of the second argument to the function
     * @param <X3> the type of the third argument to the function
     * @param <R>  the result type of the function
     * @param triFunc the TriFunction to be narrowed, with potentially wider generic types
     * @return a TriFunction narrowed to the specific types X1, X2, X3, and R
     * @throws NullPointerException if {@code triFunc} is {@code null}
     */
    @SuppressWarnings("unchecked")
    static <X1, X2, X3, R> TriFunction<X1, X2, X3, R> narrow(TriFunction<? extends X1, ? extends X2, ? extends X3, ? extends R> triFunc) {
        CheckUtils.NullCheck.requireNonNull(triFunc);
        return (TriFunction<X1, X2, X3, R>) triFunc;
    }


}