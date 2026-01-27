package io.github.piscescup.mc.fabric.interfaces.exfunction;

import io.github.piscescup.mc.fabric.utils.CheckUtils;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents an operation that accepts four input arguments and returns no result.
 * This is the four-arity specialization of {@link java.util.function.Consumer}.
 * Unlike most other functional interfaces, {@code QuadConsumer} is expected
 * to operate via side-effects.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Object, Object, Object, Object)}.
 *
 * @param <X1> the type of the first argument to the operation
 * @param <X2> the type of the second argument to the operation
 * @param <X3> the type of the third argument to the operation
 * @param <X4> the type of the fourth argument to the operation
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
@FunctionalInterface
public interface QuadConsumer<X1, X2, X3, X4> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param x1 the first input argument
     * @param x2 the second input argument
     * @param x3 the third input argument
     * @param x4 the fourth input argument
     */
    void accept(X1 x1, X2 x2, X3 x3, X4 x4);

    /**
     * Returns a composed {@code QuadConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation. If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code QuadConsumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is {@code null}
     */
    default QuadConsumer<X1, X2, X3, X4> andThen(QuadConsumer<? super X1, ? super X2, ? super X3, ? super X4> after) {
        CheckUtils.NullCheck.requireNonNull(after);
        return (x1, x2, x3, x4) -> {
            accept(x1, x2, x3, x4);
            after.accept(x1, x2, x3, x4);
        };
    }

    /**
     * Returns a curried version of this {@code QuadConsumer} as a function that
     * accepts one argument at a time, returning functions that accept the next
     * arguments until all arguments are provided and the operation is performed.
     *
     * @return a function that takes the first argument and returns a function
     *         which takes the second argument and returns a function that takes
     *         the third argument and finally returns a consumer that accepts the
     *         fourth argument to perform the operation
     */
    default Function<X1, Function<X2, Function<X3, Consumer<X4>>>> curried() {
        return x1 -> x2 -> x3 -> x4 -> this.accept(x1, x2, x3, x4);
    }

    /**
     * Returns a new {@code QuadConsumer} whose parameters are in reverse order compared to this one.
     * The returned consumer will accept its arguments in the order (x1, x2, x3, x4) and pass them
     * to the original consumer in the reversed order (x4, x3, x2, x1).
     *
     * @return a new {@code QuadConsumer} with reversed parameter order
     */
    default QuadConsumer<X4, X3, X2, X1> reversed() {
        return (x1, x2, x3, x4) -> accept(x4, x3, x2, x1);
    }

    /**
     * Returns a {@code QuadConsumer} that performs no operation.
     *
     * @param <X1> the type of the first argument to the operation
     * @param <X2> the type of the second argument to the operation
     * @param <X3> the type of the third argument to the operation
     * @param <X4> the type of the fourth argument to the operation
     * @return a {@code QuadConsumer} that does nothing when its {@link #accept(Object, Object, Object, Object)} method is called
     */
    static <X1, X2, X3, X4> QuadConsumer<X1, X2, X3, X4> empty() {
        return (x1, x2, x3, x4) -> {};
    }

}
