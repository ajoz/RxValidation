package io.github.ajoz.rxvalidation.internal.observers;

import io.github.ajoz.rxvalidation.ValidationObserver;
import io.reactivex.disposables.Disposable;

public class TestValidationObserver<E, T>
        implements ValidationObserver<E, T>, Disposable {

    @Override
    public void onSubscribe(final Disposable d) {

    }

    @Override
    public void onSuccess(final T value) {

    }

    @Override
    public void onFailure(final E error) {

    }

    @Override
    public void onError(final Throwable e) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean isDisposed() {
        return false;
    }
}
