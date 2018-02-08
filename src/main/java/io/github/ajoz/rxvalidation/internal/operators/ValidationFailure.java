package io.github.ajoz.rxvalidation.internal.operators;

import io.github.ajoz.rxvalidation.Validation;
import io.github.ajoz.rxvalidation.ValidationObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposables;

public class ValidationFailure<E, T> extends Validation<E, T> {
    final E error;

    public ValidationFailure(final E error) {
        this.error = error;
    }

    @Override
    protected void subscribeActual(@NonNull ValidationObserver<? super E, ? super T> observer) {
        observer.onSubscribe(Disposables.disposed());
        observer.onFailure(error);
    }
}
