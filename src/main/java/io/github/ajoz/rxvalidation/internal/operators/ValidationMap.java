package io.github.ajoz.rxvalidation.internal.operators;

import io.github.ajoz.rxvalidation.Validation;
import io.github.ajoz.rxvalidation.ValidationObserver;
import io.github.ajoz.rxvalidation.ValidationSource;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.ProtocolViolationException;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;

import java.util.function.Function;

public class ValidationMap<E, T, R> extends Validation<E, R> {

    final ValidationSource<E, T> source;
    final Function<? super T, ? extends R> mapper;

    public ValidationMap(ValidationSource<E, T> source, Function<? super T, ? extends R> mapper) {
        this.source = source;
        this.mapper = mapper;
    }


    @Override
    protected void subscribeActual(ValidationObserver<E, ? super R> observer) {
        source.subscribe(new MapValidationObserver<E, T, R>(observer, mapper));
    }

    static final class MapValidationObserver<E, T, R> implements ValidationObserver<E, T>, Disposable {
        final ValidationObserver<E, ? super R> actual;
        final Function<? super T, ? extends R> mapper;

        @Nullable Disposable disposable;

        MapValidationObserver(ValidationObserver<E, ? super R> actual, Function<? super T, ? extends R> mapper) {
            this.actual = actual;
            this.mapper = mapper;
        }

        @Override
        public void dispose() {
            final Disposable d = disposable;
            disposable = DisposableHelper.DISPOSED;
            d.dispose();
        }

        @Override
        public boolean isDisposed() {
            return disposable.isDisposed();
        }

        @Override
        public void onSubscribe(final Disposable d) {
            ObjectHelper.requireNonNull(d, "disposable is null");

            if (disposable != null) {
                d.dispose();
                throw new ProtocolViolationException("Disposable already set!");
            }

            disposable = d;
            actual.onSubscribe(this);
        }

        @Override
        public void onSuccess(final T value) {
            R mapped;
            try {
                mapped = ObjectHelper.requireNonNull(mapper.apply(value), "mapper returned null result");
            } catch (final Throwable exc) {
                Exceptions.throwIfFatal(exc);
                actual.onError(exc);
                return;
            }

            actual.onSuccess(mapped);
        }

        @Override
        public void onFailure(final E error) {
            actual.onFailure(error);
        }

        @Override
        public void onError(final Throwable exc) {
            actual.onError(exc);
        }
    }
}
