package io.github.ajoz.rxvalidation;

import io.github.ajoz.rxvalidation.internal.operators.ValidationFailure;
import io.github.ajoz.rxvalidation.internal.operators.ValidationSuccess;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;

@SuppressWarnings("ResultOfMethodCallIgnored")
public abstract class Validation<E, T> implements ValidationSource<E, T> {
    @Override
    public final void subscribe(ValidationObserver<E, ? super T> subscriber) {
        ObjectHelper.requireNonNull(subscriber, "subscriber is null");

        //no RxJavaPlugins hooks

        try {
            subscribeActual(subscriber);
        } catch (NullPointerException ex) {
            throw ex;
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            final NullPointerException npe = new NullPointerException("subscribeActual failed");
            npe.initCause(ex);
            throw npe;
        }
    }

    protected abstract void subscribeActual(@NonNull ValidationObserver<E, ? super T> observer);

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <E, T> Validation<E, T> success(final T value) {
        ObjectHelper.requireNonNull(value, "value is null");
        //no RxJavaPlugins hooks
        return new ValidationSuccess<>(value);
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <E, T> Validation<E, T> failure(final E error) {
        ObjectHelper.requireNonNull(error, "error is null");
        return new ValidationFailure<>(error);
    }
}
