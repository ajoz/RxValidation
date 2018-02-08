package io.github.ajoz.rxvalidation.utils;

import io.reactivex.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ListUtils {
    private ListUtils() {
    }

    @NonNull
    public static <T> List<T> union(@NonNull final List<? extends T> first,
                                    @NonNull final List<? extends T> second) {
        final List<T> result = new ArrayList<>(first);
        result.addAll(second);
        return Collections.unmodifiableList(result);
    }
}
