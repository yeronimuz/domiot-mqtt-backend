package org.domiot.backend.utils;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;

import org.awaitility.Awaitility;

public class AwaitUtils {

    /**
     * Waits until the provided supplier returns a non-empty Optional and returns the contained value.
     *
     * @param supplier     Supplier returning an Optional<T>
     * @param maxWait      Maximum duration to wait
     * @param pollInterval Poll interval
     * @param <T>          Type of the value inside the Optional
     * @return The resolved value once present
     */
    public static <T> T awaitOptional(Supplier<Optional<T>> supplier, Duration maxWait, Duration pollInterval) {
        final Object[] holder = new Object[1];
        Awaitility.await()
                .atMost(maxWait)
                .pollInterval(pollInterval)
                .until(() -> {
                    Optional<T> result = supplier.get();
                    result.ifPresent(val -> holder[0] = val);
                    return result.isPresent();
                });
        @SuppressWarnings("unchecked")
        T result = (T) holder[0];
        return result;
    }
}
