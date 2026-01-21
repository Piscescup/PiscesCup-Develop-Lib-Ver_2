package io.github.piscescup.mc.fabric.utils;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author REN YuanTong
 * @since 1.0.0
 */
public final class CheckUtils {
    private CheckUtils() {}

    public static final
    class NullCheck {
        public static final Function<Object, String> OBJECT_NULL_MESSAGE_FUNC =
            obj -> "Object: " + obj.toString() + " must not be null";

        public static final Function<Object, String> ITERABLE_NULL_MESSAGE_FUNC =
            obj -> "Iterable: " + obj.toString() + " must not contain null elements";

        public static final Function<Object, String> ARRAY_NULL_MESSAGE_FUNC =
            obj -> "Array: " + obj.toString() + " must not contain null elements";

        private NullCheck() {}

        public static boolean isNull(Object obj) {
            return obj == null;
        }

        public static boolean nonNull(Object obj) {
            return obj != null;
        }

        public static <T> T requireNonNull(T obj, String message) {
            String mes = message == null ? OBJECT_NULL_MESSAGE_FUNC.apply(obj) : message;
            if (obj == null) {
                throw new NullPointerException(message);
            }
            return obj;
        }

        public static <T> T requireNonNull(T obj) {
            return requireNonNull(obj, OBJECT_NULL_MESSAGE_FUNC.apply(obj));
        }

        public static <T> T requireNonNullOrElse(T obj, T defaultObj) {
            return isNull(obj) ? defaultObj : obj;
        }

        public static <T> T requireNonNullOrElse(T obj, Supplier<T> defaultObjSupplier) {
            return isNull(obj) ? defaultObjSupplier.get() : obj;
        }

        public static <T> T requireNonNullOrThrow(T obj, Throwable throwable) {
            if (isNull(obj)) {
                throw new RuntimeException(throwable);
            }
            return obj;
        }

        public static <T> T requireNonNullOrThrow(T obj, Supplier<Throwable> throwableSupplier) {
            if (isNull(obj)) {
                throw new RuntimeException(throwableSupplier.get());
            }
            return obj;
        }

        public static boolean containsNull(Iterable<?> iterable) {
            if (iterable == null) {
                return true;
            }
            for (Object o : iterable) {
                if (isNull(o)) {
                    return true;
                }
            }
            return false;
        }

        public static boolean allNonNull(Iterable<?> iterable) {
            return !containsNull(iterable);
        }

        public static <T> Iterable<T> requireAllNonNull(Iterable<T> iterable, String message) {
            requireNonNull(iterable, OBJECT_NULL_MESSAGE_FUNC.apply(iterable));
            for (T obj : iterable) {
                requireNonNull(obj, message);
            }
            return iterable;
        }

        public static <T> Iterable<T> requireAllNonNull(Iterable<T> iterable) {
            return requireAllNonNull(iterable, ITERABLE_NULL_MESSAGE_FUNC.apply(iterable));
        }

        public static <T> Iterable<T> requireAllNonNullOrElse(Iterable<T> iterable, Iterable<T> defaultIterable) {
            return containsNull(iterable) ? defaultIterable : iterable;
        }

        public static <T> T[] requireAllNonNull(T[] array, String message) {
            for (T obj : array) {
                requireNonNull(obj, message);
            }
            return array;
        }

        public static <T> T[] requireAllNonNull(T[] array) {
            return requireAllNonNull(array, ARRAY_NULL_MESSAGE_FUNC.apply(array));
        }


    }
}
