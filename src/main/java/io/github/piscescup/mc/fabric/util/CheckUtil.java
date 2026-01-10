package io.github.piscescup.mc.fabric.util;

import java.util.Arrays;
import java.util.function.Function;

/**
 * <h2>Description</h2>
 *
 * <h2>Usages</h2>
 *
 * @author REN YuanTong
 * @Date 2025-12-18
 * @since
 */
public final class CheckUtil {
    private CheckUtil() {
        throw new UnsupportedOperationException("This is no instance for " + CheckUtil.class.getCanonicalName());
    }

    public static
    final class NullCheck {
        public static final Function<Object, String> DEFAULT_OBJECT_NULL_MESSAGE =
            obj -> obj.getClass().getCanonicalName() + " must not be null";

        public static final Function<Object, String> DEFAULT_ARRAY_CONTAINS_NULL_MESSAGE =
            obj -> obj.getClass().getCanonicalName() + " must not contain null element(s)";

        public static final Function<Object, String> DEFAULT_ITERABLE_CONTAINS_NULL_MESSAGE =
            obj -> obj.getClass().getCanonicalName() + " must not contain null element(s)";

        private NullCheck() {
            throw new UnsupportedOperationException("This is no instance for " + NullCheck.class.getCanonicalName());
        }

        public static <T> T requireNotNull(T obj, String message) {
            if (obj == null) {
                throw new NullPointerException(message);
            }
            return obj;
        }

        public static <T> T requireNotNull(T obj) {
            return requireNotNull(obj, DEFAULT_OBJECT_NULL_MESSAGE.apply(obj));
        }

        public static <T> T[] requireAllNotNull(T[] arys, String message) {
            if (containsNull(arys)) {
                throw new NullPointerException(message);
            }
            return arys;
        }

        public static <T> T[] requireAllNotNull(T[] arys) {
            return requireAllNotNull(arys, DEFAULT_ARRAY_CONTAINS_NULL_MESSAGE.apply(arys));
        }

        public static <T> Iterable<T> requireAllNotNull(Iterable<T> arys, String message) {
            if (containsNull(arys)) {
                throw new NullPointerException(message);
            }
            return arys;
        }

        public static <T> Iterable<T> requireAllNotNull(Iterable<T> arys) {
            return requireAllNotNull(arys, DEFAULT_ITERABLE_CONTAINS_NULL_MESSAGE.apply(arys));
        }

        public static <T> boolean isNull(T obj) {
            return obj == null;
        }

        public static <T> boolean nonNull(T obj) {
            return obj != null;
        }

        public static <T> boolean containsNull(T[] array) {
            requireNotNull(array);
            return Arrays.stream(array).anyMatch(NullCheck::isNull);
        }

        public static <T> boolean allNotNull(T[] aray) {
            requireNotNull(aray);
            return Arrays.stream(aray).allMatch(NullCheck::nonNull);
        }

        public static <T> boolean containsNull(Iterable<T> iterable) {
            requireNotNull(iterable);
            for (T obj : iterable) {
                if (isNull(obj)) {
                    return true;
                }
            }
            return false;
        }

        public static <T> boolean allNotNull(Iterable<T> iterable) {
            requireNotNull(iterable);
            for (T obj : iterable) {
                if (isNull(obj)) {
                    return false;
                }
            }
            return true;
        }



    }
}
