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
    protected void subscribeActual(@NonNull ValidationObserver<E, ? super T> observer) {
        observer.onSubscribe(Disposables.disposed());
        observer.onFailure(error);
    }
}
