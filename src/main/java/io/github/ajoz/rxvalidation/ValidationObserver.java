package io.github.ajoz.rxvalidation;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

// Can have only 1 response a Success or a Failure or an Error
// Failure is not the same as Error
public interface ValidationObserver<E, T> {
    void onSubscribe(@NonNull Disposable d);
    void onSuccess(@NonNull T t);
    void onFailure(@NonNull E e);
    void onError(@NonNull Throwable e);
}
