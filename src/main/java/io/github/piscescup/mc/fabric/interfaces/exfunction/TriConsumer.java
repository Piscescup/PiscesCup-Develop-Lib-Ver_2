package io.github.piscescup.mc.fabric.interfaces.exfunction;

import io.github.piscescup.mc.fabric.utils.CheckUtils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents an operation that accepts three input arguments and returns no result.
 * This is the three-arity specialization of {@link Consumer}.
 * Unlike most other functional interfaces, {@code TriConsumer} is expected
 * to operate via side-effects.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Object, Object, Object)}.
 *
 * @param <X1> the type of the first argument to the operation
 * @param <X2> the type of the second argument to the operation
 * @param <X3> the type of the third argument to the operation
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
@FunctionalInterface
public interface TriConsumer<X1, X2, X3> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param x1 the first input argument
     * @param x2 the second input argument
     * @param x3 the third input argument
     */
    void accept(X1 x1, X2 x2, X3 x3);

    /**
     * Returns a composed {@code TriConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation. If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code TriConsumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default TriConsumer<X1, X2, X3> andThen(TriConsumer<? super X1, ? super X2, ? super X3> after) {
        CheckUtils.NullCheck.requireNonNull(after);
        return (X1 x1, X2 x2, X3 x3) -> {
            accept(x1, x2, x3);
            after.accept(x1, x2, x3);
        };
    }

    /**
     * Returns a curried version of this {@code TriConsumer} as a function that
     * accepts one argument at a time, returning functions that accept the next
     * arguments until all arguments are provided and the operation is performed.
     *
     * @return a function that takes the first argument and returns a function
     *         which takes the second argument and returns a consumer that finally
     *         accepts the third argument to perform the operation
     */
    default Function<X1, Function<X2, Consumer<X3>>> curried() {
        return x1 -> x2 -> x3 -> accept(x1, x2, x3);
    }

    /**
     * Returns a {@code TriConsumer} that performs no operation.
     *
     * @param <X1> the type of the first argument to the operation
     * @param <X2> the type of the second argument to the operation
     * @param <X3> the type of the third argument to the operation
     * @return a {@code TriConsumer} that does nothing when its {@link #accept(Object, Object, Object)} method is called
     */
    static <X1, X2, X3> TriConsumer<X1, X2, X3> empty() {
        return (X1, X2, X3) -> {};
    }
}