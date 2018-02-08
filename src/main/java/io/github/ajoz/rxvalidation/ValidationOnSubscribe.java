package io.github.ajoz.rxvalidation;

import io.reactivex.annotations.NonNull;

public interface ValidationOnSubscribe<E, T> {
    // Called for each ValidationObserver that subscribes.
    void subscribe(@NonNull ValidationEmitter<E, T> emitter) throws Exception;
}
