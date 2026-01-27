package io.github.piscescup.mc.fabric.interfaces.exfunction;


import io.github.piscescup.mc.fabric.interfaces.Memorized;
import io.github.piscescup.mc.fabric.interfaces.entries.QuinEntry;
import io.github.piscescup.mc.fabric.utils.CheckUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a predicate (boolean-valued function) of five arguments.
 * This is the five-arity specialization of {@link Predicate}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #test(Object, Object, Object, Object, Object)}.
 *
 * @param <X1> the type of the first argument to the predicate
 * @param <X2> the type of the second argument to the predicate
 * @param <X3> the type of the third argument to the predicate
 * @param <X4> the type of the fourth argument to the predicate
 * @param <X5> the type of the fifth argument to the predicate
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
@FunctionalInterface
public interface QuinPredicate<X1, X2, X3, X4, X5> {

    /**
     * Evaluates this predicate on the given arguments.
     *
     * @param x1 the first input argument
     * @param x2 the second input argument
     * @param x3 the third input argument
     * @param x4 the fourth input argument
     * @param x5 the fifth input argument
     * @return {@code true} if the input arguments match the predicate,
     * otherwise {@code false}
     */
    boolean test(X1 x1, X2 x2, X3 x3, X4 x4, X5 x5);

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * AND of this predicate and another.
     *
     * @param other a predicate that will be logically-ANDed with this predicate
     * @return a composed predicate that represents the short-circuiting logical
     * AND of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    default QuinPredicate<X1, X2, X3, X4, X5> and(QuinPredicate<? super X1, ? super X2, ? super X3, ? super X4, ? super X5> other) {
        CheckUtils.NullCheck.requireNonNull(other);
        return (x1, x2, x3, x4, x5) -> test(x1, x2, x3, x4, x5) && other.test(x1, x2, x3, x4, x5);
    }

    /**
     * Returns a predicate that represents the logical negation of this predicate.
     *
     * @return a predicate that represents the logical negation of this predicate
     */
    default QuinPredicate<X1, X2, X3, X4, X5> negate() {
        return (x1, x2, x3, x4, x5) -> !test(x1, x2, x3, x4, x5);
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * OR of this predicate and another.
     *
     * @param other a predicate that will be logically-ORed with this predicate
     * @return a composed predicate that represents the short-circuiting logical
     * OR of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    default QuinPredicate<X1, X2, X3, X4, X5> or(QuinPredicate<? super X1, ? super X2, ? super X3, ? super X4, ? super X5> other) {
        CheckUtils.NullCheck.requireNonNull(other);
        return (x1, x2, x3, x4, x5) -> test(x1, x2, x3, x4, x5) || other.test(x1, x2, x3, x4, x5);
    }

    /**
     * Transforms this multi-argument predicate into a sequence of single-argument functions.
     * The final function in the chain returns a standard {@link Predicate}.
     *
     * @return the curried version of this predicate
     */
    default Function<X1, Function<X2, Function<X3, Function<X4, Predicate<X5>>>>> curried() {
        return x1 -> x2 -> x3 -> x4 -> x5 -> test(x1, x2, x3, x4, x5);
    }

    /**
     * Returns a new {@code QuinPredicate} that, when tested, will pass the arguments in reverse order to this predicate.
     *
     * @return a new {@code QuinPredicate} with reversed argument order.
     */
    default QuinPredicate<X5, X4, X3, X2, X1> reversed() {
        return (x5, x4, x3, x2, x1) -> test(x1, x2, x3, x4, x5);
    }

    /**
     * Checks if this instance is memorized, meaning it implements the {@link Memorized} interface.
     *
     * @return {@code true} if this instance is an implementation of the {@link Memorized} interface, otherwise {@code false}
     */
    default boolean isMemorized() {
        return this instanceof Memorized;
    }

    /**
     * Returns a memorized version of this predicate. If the predicate is already
     * memorized, it returns the current instance.
     *
     * @return a memorized version of this predicate
     */
    default QuinPredicate<X1, X2, X3, X4, X5> memorized() {
        if (isMemorized()) return this;

        final Map<QuinEntry<X1, X2, X3, X4, X5>, Boolean> cache = new ConcurrentHashMap<>();

        return ( QuinPredicate<X1, X2, X3, X4, X5> & Memorized ) (x1, x2, x3, x4, x5) -> {
            QuinEntry<X1, X2, X3, X4, X5> entry = new QuinEntry<>(x1, x2, x3, x4, x5);
            return cache.computeIfAbsent(entry, e -> this.test(e.x1(), e.x2(), e.x3(), e.x4(), e.x5()));
        };
    }

    /**
     * Returns a quin predicate that always evaluates to {@code true} regardless of the input arguments.
     *
     * @param <X1> the type of the first argument to the predicate
     * @param <X2> the type of the second argument to the predicate
     * @param <X3> the type of the third argument to the predicate
     * @param <X4> the type of the fourth argument to the predicate
     * @param <X5> the type of the fifth argument to the predicate
     * @return a quin predicate that always returns {@code true}
     */
    static <X1, X2, X3, X4, X5> QuinPredicate<X1, X2, X3, X4, X5> always() {
        return (x1, x2, x3, x4, x5) -> true;
    }

    /**
     * Returns a quin predicate that always evaluates to {@code false} regardless of the input arguments.
     *
     * @param <X1> the type of the first argument to the predicate
     * @param <X2> the type of the second argument to the predicate
     * @param <X3> the type of the third argument to the predicate
     * @param <X4> the type of the fourth argument to the predicate
     * @param <X5> the type of the fifth argument to the predicate
     * @return a quin predicate that always returns {@code false}
     */
    static <X1, X2, X3, X4, X5> QuinPredicate<X1, X2, X3, X4, X5> never() {
        return (x1, x2, x3, x4, x5) -> false;
    }
}