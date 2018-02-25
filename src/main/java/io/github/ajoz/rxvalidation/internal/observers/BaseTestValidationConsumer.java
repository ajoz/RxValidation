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
package io.github.ajoz.rxvalidation.internal.observers;

import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.internal.util.VolatileSizeArrayList;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public abstract class BaseTestValidationConsumer<S, F, U extends BaseTestValidationConsumer<S, F, U>>
        implements Disposable {
    /**
     * The latch that indicates an onError or onComplete has been called.
     */
    protected final CountDownLatch done;
    /**
     * The list of values received.
     */
    protected final List<S> successes;
    /**
     * The list of failures received
     */
    protected final List<List<F>> failures;
    /**
     * The list of errors received.
     */
    protected final List<Throwable> errors;
    /**
     * The last thread seen by the observer.
     */
    protected Thread lastThread;

    public BaseTestValidationConsumer() {
        this.successes = new VolatileSizeArrayList<S>();
        this.failures = new VolatileSizeArrayList<List<F>>();
        this.errors = new VolatileSizeArrayList<Throwable>();
        this.done = new CountDownLatch(1);
    }

    public final List<S> successes() {
        return successes;
    }

    public final List<List<F>> failures() {
        return failures;
    }

    public final List<Throwable> errors() {
        return errors;
    }

    public final boolean isTerminated() {
        return done.getCount() == 0;
    }

    @SuppressWarnings("unchecked")
    public final U await() throws InterruptedException {
        if (done.getCount() == 0) {
            return (U) this;
        }

        done.await();
        return (U) this;
    }

    protected final AssertionError fail(final String message) {
        final StringBuilder messageBuilder = new StringBuilder();
        messageBuilder
                .append(message)
                .append(" (")
                .append("latch = ")
                .append(done.getCount())
                .append(", ")
                .append("successes = ")
                .append(successes.size())
                .append(", ")
                .append("errors = ")
                .append(errors.size())
                .append(", ");

        if (isDisposed()) {
            messageBuilder.append(", disposed!");
        }

        messageBuilder.append(')');

        final AssertionError ae = new AssertionError(messageBuilder.toString());
        if (!errors.isEmpty()) {
            if (errors.size() == 1) {
                ae.initCause(errors.get(0));
            } else {
                final CompositeException ce = new CompositeException(errors);
                ae.initCause(ce);
            }
        }
        return ae;
    }


}
