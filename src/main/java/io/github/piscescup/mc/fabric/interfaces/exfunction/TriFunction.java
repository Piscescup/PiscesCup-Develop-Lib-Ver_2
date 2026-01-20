package io.github.piscescup.mc.fabric.interfaces.exfunction;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents a function that accepts three arguments and produces a result.
 * This is the three-arity specialization of {@link Function}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object, Object, Object)}.
 *
 * @param <X> the type of the first argument to the function
 * @param <Y> the type of the second argument to the function
 * @param <Z> the type of the third argument to the function
 * @param <R> the type of the result of the function
 *
 * @see Function
 * @see BiFunction
 * @author REN YuanTong
 * @since 1.0.0
 */
@FunctionalInterface
public interface TriFunction<X, Y, Z, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param x the first function argument
     * @param y the second function argument
     * @param z the third function argument
     * @return the function result
     */
    R apply(X x, Y y, Z z);

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
     * @throws NullPointerException if after is null
     */
    default <V> TriFunction<X, Y, Z, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (X x, Y y, Z z) -> after.apply(apply(x, y, z));
    }

    /**
     * Returns a {@link BiFunction} that represents the partial application of
     * this function with the first argument fixed.
     *
     * @param x the fixed first argument
     * @return a composed {@code BiFunction} that takes the remaining two arguments
     */
    default BiFunction<Y, Z, R> simplifiedByX(X x) {
        return (y, z) -> apply(x, y, z);
    }

    /**
     * Returns a {@link BiFunction} that represents the partial application of
     * this function with the second argument (Y) fixed.
     *
     * @param y the fixed second argument
     * @return a composed {@code BiFunction} that takes the remaining two arguments (X, Z)
     */
    default BiFunction<X, Z, R> simplifiedByY(Y y) {
        return (x, z) -> apply(x, y, z);
    }

    /**
     * Returns a {@link BiFunction} that represents the partial application of
     * this function with the third argument (Z) fixed.
     *
     * @param z the fixed third argument
     * @return a composed {@code BiFunction} that takes the remaining two arguments (X, Y)
     */
    default BiFunction<X, Y, R> simplifiedByZ(Z z) {
        return (x, y) -> apply(x, y, z);
    }

    /**
     * Returns a {@link Function} that represents the partial application of
     * this function with the first two arguments (X, Y) fixed.
     *
     * @param x the fixed first argument
     * @param y the fixed second argument
     * @return a composed {@code Function} that takes the remaining argument (Z)
     */
    default Function<Z, R> simplifiedByXY(X x, Y y) {
        return (z) -> apply(x, y, z);
    }

    /**
     * Returns a {@link Function} that represents the partial application of
     * this function with the last two arguments (Y, Z) fixed.
     *
     * @param y the fixed second argument
     * @param z the fixed third argument
     * @return a composed {@code Function} that takes the remaining argument (X)
     */
    default Function<X, R> simplifiedByYZ(Y y, Z z) {
        return (x) -> apply(x, y, z);
    }

    /**
     * Returns a {@link Function} that represents the partial application of
     * this function with the first and last arguments (X, Z) fixed.
     *
     * @param x the fixed first argument
     * @param z the fixed third argument
     * @return a composed {@code Function} that takes the remaining argument (Y)
     */
    default Function<Y, R> simplifiedByXZ(X x, Z z) {
        return (y) -> apply(x, y, z);
    }

    /**
     * Returns a curried version of this function.
     *
     * <p>The returned function chain allows for providing arguments one by one:
     * {@code x -> y -> z -> apply(x, y, z)}.
     *
     * @return a curried function chain of this function
     */
    default Function<X, Function<Y, Function<Z, R>>> curry() {
        return x -> y -> z -> apply(x, y, z);
    }


}