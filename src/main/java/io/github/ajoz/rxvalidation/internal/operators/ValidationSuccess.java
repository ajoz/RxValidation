package io.github.ajoz.rxvalidation.internal.operators;

import io.github.ajoz.rxvalidation.Validation;
import io.github.ajoz.rxvalidation.ValidationObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposables;

public final class ValidationSuccess<E, T> extends Validation<E, T> {
    final T value;

    public ValidationSuccess(final T value) {
        this.value = value;
    }

    @Override
    protected void subscribeActual(@NonNull final ValidationObserver<E, ? super T> observer) {
        observer.onSubscribe(Disposables.disposed());
        observer.onSuccess(value);
    }
}
