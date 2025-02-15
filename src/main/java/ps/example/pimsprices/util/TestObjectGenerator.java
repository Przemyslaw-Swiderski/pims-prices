package ps.example.pimsprices.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TestObjectGenerator {

    private static final Random RANDOM = new Random();

    public static <T> T generate(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            T instance = constructor.newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = generateRandomValue(field.getType());
                field.set(instance, value);
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate test object for class: " + clazz.getSimpleName(), e);
        }
    }

    private static Object generateRandomValue(Class<?> type) {
        if (type == String.class) {
            return "Test-" + UUID.randomUUID();
        } else if (type == Long.class || type == long.class) {
            return RANDOM.nextLong(1, 1000);
        } else if (type == Integer.class || type == int.class) {
            return RANDOM.nextInt(1, 1000);
        } else if (type == BigDecimal.class) {
            return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 1000)).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else if (type == Set.class) {
            return new HashSet<>();
        } else if (type == List.class) {
            return new ArrayList<>();
        } else if (Enum.class.isAssignableFrom(type)) {
            Object[] enumConstants = type.getEnumConstants();
            return enumConstants[RANDOM.nextInt(enumConstants.length)];
        }
        return null;
    }
}

