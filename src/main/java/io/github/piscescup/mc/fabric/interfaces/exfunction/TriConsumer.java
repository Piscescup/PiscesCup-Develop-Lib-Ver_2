package io.github.piscescup.mc.fabric.interfaces.exfunction;

import io.github.piscescup.mc.fabric.utils.CheckUtils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents an operation that accepts three input arguments and returns no
 * result. Unlike most other functional interfaces, {@code TriConsumer} is expected
 * to operate via side-effects.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept}.
 *
 * @param <X> The type of the first argument to the operation
 * @param <Y> The type of the second argument to the operation
 * @param <Z> The type of the third argument to the operation
 *
 * @since 1.0.0
 * @author REN YuanTong
 */
@FunctionalInterface
public interface TriConsumer<X, Y, Z> {
    /**
     * Performs this operation on the given arguments.
     *
     * @param x the first input argument
     * @param y the second input argument
     * @param z the third input argument
     */
    void accept(X x, Y y, Z z);

    /**
     * Returns a composed {@code TriConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code TriConsumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default TriConsumer<X, Y, Z> andThen(TriConsumer<? super X, ? super Y, ? super Z> after) {
        CheckUtils.NullCheck.requireNonNull(after);
        return (X x, Y y, Z z) -> { accept(x, y, z); after.accept(x, y, z); };
    }

    /**
     * Returns a {@link BiConsumer} that represents the partial application of
     * this consumer with the first argument fixed.
     *
     * @param x the fixed first argument
     * @return a composed {@code BiConsumer} that takes the remaining two arguments
     */
    default BiConsumer<Y, Z> simplifiedByX(X x) {
        return (y, z) -> accept(x, y, z);
    }

    /**
     * Returns a {@link BiConsumer} that represents the partial application of
     * this consumer with the second argument fixed.
     * @param y the fixed second argument
     * @return a composed {@code BiConsumer} that takes the remaining two arguments
     */
    default BiConsumer<X, Z> simplifiedByY(Y y) {
        return (x, z) -> accept(x, y, z);
    }

    /**
     * Returns a {@link BiConsumer} that represents the partial application of
     * this consumer with the third argument fixed.
     *
     * @param z the fixed third argument
     * @return a composed {@code BiConsumer} that takes the remaining two arguments
     */
    default BiConsumer<X, Y> simplifiedByZ(Z z) {
        return (x, y) -> accept(x, y, z);
    }

    /**
     * Returns a {@link Consumer} that represents the partial application of
     * this consumer with the first two arguments fixed.
     *
     * @param x the fixed first argument
     * @param y the fixed second argument
     * @return a composed {@code Consumer} that takes the remaining argument
     */
    default Consumer<Z> simplifiedByXY(X x, Y y) {
        return (z) -> accept(x, y, z);
    }

    /**
     * Returns a {@link Consumer} that represents the partial application of
     * this consumer with the first and third arguments fixed.
     *
     * @param x the fixed first argument
     * @param z the fixed third argument
     * @return a composed {@code Consumer} that takes the remaining argument
     */
    default Consumer<Y> simplifiedByXZ(X x, Z z) {
        return (y) -> accept(x, y, z);
    }

    /**
     * Returns a {@link Consumer} that represents the partial application of
     * this consumer with the second and third arguments fixed.
     *
     * @param y the fixed second argument
     * @param z the fixed third argument
     * @return a composed {@code Consumer} that takes the remaining argument
     */
    default Consumer<X> simplifiedByYZ(Y y, Z z) {
        return (x) -> accept(x, y, z);
    }

    /**
     * Returns a curried version of this consumer.
     *
     * <p>The returned function chain allows for providing arguments one by one,
     * ultimately performing the action when the last argument is provided.
     *
     * @return a curried function chain of this consumer
     */
    default Function<X, Function<Y, Consumer<Z>>> curry() {
        return x -> y -> z -> accept(x, y, z);
    }
}
