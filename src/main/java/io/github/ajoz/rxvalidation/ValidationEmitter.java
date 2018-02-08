package io.github.ajoz.rxvalidation;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;

public interface ValidationEmitter<E, T> {
    // signals a success value, cannot be null!
    void onSuccess(@NonNull T t);

    // signals a failure values, cannot be null!
    void onFailure(@NonNull E e);

    // signals an exception, cannot be null!
    void onError(@NonNull Throwable t);

    // Sets a Disposable on this emitter; any previous Disposable
    // or Cancellation will be unsubscribed/cancelled.
    // null is allowed!!
    void setDisposable(@Nullable Disposable s);

    // Sets a Cancellable on this emitter; any previous Disposable
    // or Cancellation will be unsubscribed/cancelled.
    // null is allowed!!
    void setCancellable(@Nullable Cancellable c);

    // Returns true if the downstream cancelled the sequence
    boolean isDisposed();
}
