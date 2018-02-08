package io.github.ajoz.rxvalidation.utils;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public final class Functions2 {
    public static <A, B> Function<A, Function<B, A>> constant() {
        return new Function<A, Function<B, A>>() {
            @Override
            public Function<B, A> apply(@NonNull final A a) throws Exception {
                return new Function<B, A>() {
                    @Override
                    public A apply(@NonNull final B ignored) throws Exception {
                        return a;
                    }
                };
            }
        };
    }
}
