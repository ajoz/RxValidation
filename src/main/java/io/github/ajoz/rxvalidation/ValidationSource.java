package io.github.ajoz.rxvalidation;

import io.reactivex.annotations.NonNull;

public interface ValidationSource<E, T> {
    void subscribe(@NonNull ValidationObserver<E, ? super T> observer);
}
