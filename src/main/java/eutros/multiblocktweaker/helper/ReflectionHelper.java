package eutros.multiblocktweaker.helper;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

public class ReflectionHelper {

    private static Map<Class<?>, Map<String, MethodHandle>> handles = new IdentityHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T, C> T getPrivate(Class<? super C> fieldClass, String fieldName, C object) throws ClassCastException {
        try {
            return (T) handles
                    .computeIfAbsent(fieldClass, c -> new HashMap<>())
                    .computeIfAbsent(fieldName, computeHandle(fieldClass, fieldName))
                    .invoke(object);
        } catch (Error | RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T, C> T getStatic(Class<? super C> fieldClass, String fieldName) throws ClassCastException {
        try {
            return (T) handles
                    .computeIfAbsent(fieldClass, c -> new HashMap<>())
                    .computeIfAbsent(fieldName, computeHandle(fieldClass, fieldName))
                    .invoke();
        } catch (Error | RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static <C> Function<String, MethodHandle> computeHandle(Class<? super C> fieldClass, String fieldName) {
        return p -> {
            try {
                return MethodHandles.publicLookup()
                        .unreflectGetter(
                                Preconditions.checkNotNull(
                                        FieldUtils.getField(
                                                fieldClass,
                                                fieldName,
                                                true
                                        ),
                                        "Couldn't find field %s of %s.",
                                        fieldName, fieldClass
                                )
                        );
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(String.format("Couldn't access field %s of %s", fieldName, fieldClass), e);
            }
        };
    }

}
