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

package io.github.ajoz.rxvalidation.internal.operators;

import io.github.ajoz.rxvalidation.ValidationObserver;
import io.github.ajoz.rxvalidation.ValidationSource;
import io.github.ajoz.rxvalidation.Verification;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.ProtocolViolationException;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;

public class ValidationToSingle<E, T> extends Single<Verification<E, T>> {
    final ValidationSource<E, T> source;

    public ValidationToSingle(@NonNull final ValidationSource<E, T> source) {
        this.source = source;
    }

    @Override
    protected void subscribeActual(@NonNull final SingleObserver<? super Verification<E, T>> observer) {
        source.subscribe(new ToSingleValidationSubscriber<E, T>(observer));
    }

    static final class ToSingleValidationSubscriber<E, T> implements ValidationObserver<E, T>, Disposable {
        final SingleObserver<? super Verification<E, T>> actual;

        Disposable disposable;

        public ToSingleValidationSubscriber(@NonNull final SingleObserver<? super Verification<E, T>> observer) {
            this.actual = observer;
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
            disposable = DisposableHelper.DISPOSED;
            actual.onSuccess(Verification.<E, T>success(value));
        }

        @Override
        public void onFailure(final E errors) {
            disposable = DisposableHelper.DISPOSED;
            actual.onSuccess(Verification.<E, T>failure(errors));
        }

        @Override
        public void onError(final Throwable e) {
            disposable = DisposableHelper.DISPOSED;
            actual.onError(e);
        }

        @Override
        public void dispose() {
            disposable.dispose();
            disposable = DisposableHelper.DISPOSED;
        }

        @Override
        public boolean isDisposed() {
            return disposable.isDisposed();
        }
    }
}
