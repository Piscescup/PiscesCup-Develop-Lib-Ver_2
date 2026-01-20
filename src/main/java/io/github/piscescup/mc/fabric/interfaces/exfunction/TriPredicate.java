package io.github.piscescup.mc.fabric.interfaces.exfunction;

import io.github.piscescup.mc.fabric.utils.CheckUtils;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a predicate (boolean-valued function) of three arguments. This is
 * the three-arity specialization of {@link Predicate}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #test(Object, Object, Object)}.
 *
 * @param <X> the type of the first argument to the predicate
 * @param <Y> the type of the second argument to the predicate
 * @param <Z> the type of the third argument to the predicate
 *
 * @see Predicate
 * @see BiPredicate
 * @author REN YuanTong
 * @since 1.0
 */
@FunctionalInterface
public interface TriPredicate<X, Y, Z> {

    /**
     * Evaluates this predicate on the given arguments.
     *
     * @param x the first input argument
     * @param y the second input argument
     * @param z the third input argument
     * @return {@code true} if the input arguments match the predicate,
     * otherwise {@code false}
     */
    boolean test(X x, Y y, Z z);

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * AND of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is {@code false}, then the {@code other}
     * predicate is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ANDed with this
     * predicate
     * @return a composed predicate that represents the short-circuiting logical
     * AND of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    default TriPredicate<X, Y, Z> and(TriPredicate<? super X, ? super Y, ? super Z> other) {
        CheckUtils.NullCheck.requireNonNull(other);
        return (X x, Y y, Z z) -> test(x, y, z) && other.test(x, y, z);
    }

    /**
     * Returns a predicate that represents the logical negation of this
     * predicate.
     *
     * @return a predicate that represents the logical negation of this
     * predicate
     */
    default TriPredicate<X, Y, Z> negate() {
        return (X x, Y y, Z z) -> !test(x, y, z);
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * OR of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is {@code true}, then the {@code other}
     * predicate is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ORed with this
     * predicate
     * @return a composed predicate that represents the short-circuiting logical
     * OR of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    default TriPredicate<X, Y, Z> or(TriPredicate<? super X, ? super Y, ? super Z> other) {
        CheckUtils.NullCheck.requireNonNull(other);
        return (X x, Y y, Z z) -> test(x, y, z) || other.test(x, y, z);
    }

    /**
     * Returns a {@link BiPredicate} that represents the partial application of
     * this predicate with the first argument fixed.
     *
     * @param x the fixed first argument
     * @return a composed {@code BiPredicate} that takes the remaining two arguments
     */
    default BiPredicate<Y, Z> simplifiedByX(X x) {
        return (y, z) -> test(x, y, z);
    }

    /**
     * Returns a {@link BiPredicate} that represents the partial application of
     * this predicate with the second argument fixed.
     *
     * @param y the fixed second argument
     * @return a composed {@code BiPredicate} that takes the remaining two arguments
     */
    default BiPredicate<X, Z> simplifiedByY(Y y) {
        return (x, z) -> test(x, y, z);
    }

    /**
     * Returns a {@link BiPredicate} that represents the partial application of
     * this predicate with the third argument fixed.
     *
     * @param z the fixed third argument
     * @return a composed {@code BiPredicate} that takes the remaining two arguments
     */
    default BiPredicate<X, Y> simplifiedByZ(Z z) {
        return (x, y) -> test(x, y, z);
    }

    /**
     * Returns a {@link Predicate} that represents the partial application of
     * this predicate with the first two arguments fixed.
     *
     * @param x the fixed first argument
     * @param y the fixed second argument
     * @return a composed {@code Predicate} that takes the remaining argument
     */
    default Predicate<Z> simplifiedByXY(X x, Y y) {
        return (z) -> test(x, y, z);
    }

    /**
     * Returns a {@link Predicate} that represents the partial application of
     * this predicate with the first and third arguments fixed.
     *
     * @param x the fixed first argument
     * @param z the fixed third argument
     * @return a composed {@code Predicate} that takes the remaining argument
     */
    default Predicate<Y> simplifiedByXZ(X x, Z z) {
        return (y) -> test(x, y, z);
    }

    /**
     * Returns a {@link Predicate} that represents the partial application of
     * this predicate with the second and third arguments fixed.
     *
     * @param y the fixed second argument
     * @param z the fixed third argument
     * @return a composed {@code Predicate} that takes the remaining argument
     */
    default Predicate<X> simplifiedByYZ(Y y, Z z) {
        return (x) -> test(x, y, z);
    }

    /**
     * Returns a curried version of this predicate.
     *
     * <p>The returned function chain allows for providing arguments one by one:
     * {@code x -> y -> z -> test(x, y, z)}.
     *
     * @return a curried function chain of this predicate
     */
    default Function<X, Function<Y, Predicate<Z>>> curry() {
        return x -> (
            y -> (
                z -> test(x, y, z)
            )
        );
    }
}