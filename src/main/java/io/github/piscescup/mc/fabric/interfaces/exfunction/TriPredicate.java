package io.github.piscescup.mc.fabric.interfaces.exfunction;

import io.github.piscescup.mc.fabric.interfaces.Memorized;
import io.github.piscescup.mc.fabric.interfaces.entries.TriEntry;
import io.github.piscescup.mc.fabric.utils.CheckUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a predicate (boolean-valued function) of three arguments.
 * This is the three-arity specialization of {@link Predicate}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #test(Object, Object, Object)}.
 *
 * @param <X1> the type of the first argument to the predicate
 * @param <X2> the type of the second argument to the predicate
 * @param <X3> the type of the third argument to the predicate
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
@FunctionalInterface
public interface TriPredicate<X1, X2, X3> {

    /**
     * Evaluates this predicate on the given arguments.
     *
     * @param x1 the first input argument
     * @param x2 the second input argument
     * @param x3 the third input argument
     * @return {@code true} if the input arguments match the predicate,
     * otherwise {@code false}
     */
    boolean test(X1 x1, X2 x2, X3 x3);

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * AND of this predicate and another.
     *
     * @param other a predicate that will be logically-ANDed with this predicate
     * @return a composed predicate that represents the short-circuiting logical
     * AND of this predicate and the {@code other} predicate
     * @throws NullPointerException if {@code other} is {@code null}
     */
    default TriPredicate<X1, X2, X3> and(TriPredicate<? super X1, ? super X2, ? super X3> other) {
        CheckUtils.NullCheck.requireNonNull(other);
        return (X1 x1, X2 x2, X3 x3) -> test(x1, x2, x3) && other.test(x1, x2, x3);
    }

    /**
     * Returns a predicate that represents the logical negation of this predicate.
     *
     * @return a predicate that represents the logical negation of this predicate
     */
    default TriPredicate<X1, X2, X3> negate() {
        return (X1 x1, X2 x2, X3 x3) -> !test(x1, x2, x3);
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * OR of this predicate and another.
     *
     * @param other a predicate that will be logically-ORed with this predicate
     * @return a composed predicate that represents the short-circuiting logical
     * OR of this predicate and the {@code other} predicate
     * @throws NullPointerException if {@code other} is {@code null}
     */
    default TriPredicate<X1, X2, X3> or(TriPredicate<? super X1, ? super X2, ? super X3> other) {
        CheckUtils.NullCheck.requireNonNull(other);
        return (X1 x1, X2 x2, X3 x3) -> test(x1, x2, x3) || other.test(x1, x2, x3);
    }

    /**
     * Returns a curried version of this predicate. The returned function takes one argument and returns
     * another function that takes the second argument, which in turn returns a predicate that takes the
     * third argument.
     *
     * @return a function that represents a curried version of this predicate
     */
    default Function<X1, Function<X2, Predicate<X3>>> curried() {
        return x1 -> x2 -> x3 -> test(x1, x2, x3);
    }

    /**
     * Returns a predicate that tests the arguments in reverse order.
     *
     * @return a new {@code TriPredicate} that evaluates this predicate with the
     *         arguments in reverse order, i.e., the first argument becomes the third,
     *         the second becomes the second, and the third becomes the first.
     */
    default TriPredicate<X3, X2, X1> reversed() {
        return (x3, x2, x1) -> this.test(x1, x2, x3);
    }

    /**
     * Checks if this predicate is an instance of the {@link Memorized} interface.
     *
     * @return {@code true} if this predicate is memorized, otherwise {@code false}
     */
    default boolean isMemorized() {
        return this instanceof Memorized;
    }

    /**
     * Returns a memorized version of this predicate. If the predicate is already
     * memorized, it returns the current instance. Otherwise, it creates a new
     * memorized predicate that caches the results of the predicate's evaluation.
     *
     * @return a memorized version of this {@code TriPredicate}
     */
    default TriPredicate<X1, X2, X3> memorized() {
        if (isMemorized()) return this;
        final Map<TriEntry<X1, X2, X3>, Boolean> cache = new ConcurrentHashMap<>();
        return (TriPredicate<X1, X2, X3> & Memorized) (X1 x1, X2  x2, X3 x3) -> {
            TriEntry<X1, X2, X3> triEntry = new TriEntry<>(x1, x2, x3);
            return cache.computeIfAbsent(triEntry, entry -> this.test(entry.x1(), entry.x2(), entry.x3()));
        };
    }

    /**
     * Returns a {@code TriPredicate} that always evaluates to {@code true} regardless of the input arguments.
     *
     * @param <X1> the type of the first argument to the predicate
     * @param <X2> the type of the second argument to the predicate
     * @param <X3> the type of the third argument to the predicate
     * @return a {@code TriPredicate} that always returns {@code true}
     */
    static <X1, X2, X3> TriPredicate<X1, X2, X3> always() {
        return (X1 x1, X2 x2, X3 x3) -> true;
    }

    /**
     * Returns a {@code TriPredicate} that always evaluates to {@code false} regardless of the input arguments.
     *
     * @param <X1> the type of the first argument to the predicate
     * @param <X2> the type of the second argument to the predicate
     * @param <X3> the type of the third argument to the predicate
     * @return a {@code TriPredicate} that always returns {@code false}
     */
    static <X1, X2, X3> TriPredicate<X1, X2, X3> never() {
        return (X1 x1, X2 x2, X3 x3) -> false;
    }

    /**
     * Returns a {@code TriPredicate} that is ensured to be non-null.
     *
     * @param <X1> the type of the first argument to the predicate
     * @param <X2> the type of the second argument to the predicate
     * @param <X3> the type of the third argument to the predicate
     * @param triPredicate the predicate to ensure non-null
     * @return the non-null {@code TriPredicate}
     * @throws NullPointerException if the provided {@code triPredicate} is {@code null}
     */
    static <X1, X2, X3> TriPredicate<X1, X2, X3> of(TriPredicate<X1, X2, X3> triPredicate) {
        CheckUtils.NullCheck.requireNonNull(triPredicate);
        return triPredicate;
    }
}