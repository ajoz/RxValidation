/*
 * Copyright (c) 2018-present, RxValidation contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
