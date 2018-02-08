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

import io.github.ajoz.rxvalidation.utils.Functions2;
import static io.github.ajoz.rxvalidation.utils.Functions2.constant;
import static io.github.ajoz.rxvalidation.utils.ListUtils.union;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.Functions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Verification<E, T> {

    @NonNull
    public abstract <R> Verification<E, R> map(@NonNull final Function<? super T, ? extends R> function);

    public abstract boolean isSuccess();

    public boolean isFailure() {
        return !isSuccess();
    }

    @NonNull
    public T getValue() {
        throw new RuntimeException("No value element available!");
    }

    @NonNull
    public List<E> getErrors() {
        throw new RuntimeException("No error elements available!");
    }

    @NonNull
    public static <E, A> Verification<E, A> success(@NonNull final A value) {
        return new Success<E, A>(value);
    }

    @NonNull
    public static <E, A> Verification<E, A> failure(@NonNull final E error) {
        return new Failure<E, A>(error);
    }

    @NonNull
    public static <E, A> Verification<E, A> failure(@NonNull final E... errors) {
        return new Failure<E, A>(errors);
    }

    public <R> Verification<E, T> apLeft(final Verification<E, R> other) {
        return ap(this.map(Functions2.<T, R>constant()), other);
    }

    public <B> Verification<E, B> apRight(final Verification<E, B> other) {
        final Function<Verification<E, T>, Verification<E, Function<B, B>>> vid = mapConst(Functions.<B>identity());
        try {
            final Verification<E, Function<B, B>> fmapped = vid.apply(this);
            return ap(fmapped, other);
        } catch (final Throwable exc) {
            throw new RuntimeException(exc);
        }
    }

    @NonNull
    public static <A, B, E> Function<Verification<E, B>, Verification<E, A>> mapConst(@NonNull final A a) {
        return new Function<Verification<E, B>, Verification<E, A>>() {
            @NonNull
            @Override
            public Verification<E, A> apply(@NonNull final Verification<E, B> validation) throws Exception {
                final Function<A, Function<B, A>> constant = constant();
                final Function<B, A> constA = constant.apply(a);
                return validation.map(constA);
            }
        };
    }

    @SuppressWarnings("ConstantConditions")
    public static <E, A, B> Verification<E, B> ap(@NonNull final Verification<E, Function<A, B>> first,
                                                  @NonNull final Verification<E, A> second) {
        // Failure e1 `ap` Failure e2 == Failure (e1 <> e2)
        if (first instanceof Failure && second instanceof Failure) {
            final Failure<E, Function<A, B>> failure1 = (Failure<E, Function<A, B>>) first;
            final Failure<E, A> failure2 = (Failure<E, A>) second;
            final List<E> e1 = failure1.errors;
            final List<E> e2 = failure2.errors;
            return new Failure<E, B>(union(e1, e2));
        }
        // Failure e1 `ap` Success _ == Failure e1
        else if (first instanceof Failure && second instanceof Success) {
            final Failure<E, Function<A, B>> failure = (Failure<E, Function<A, B>>) first;
            return new Failure<E, B>(failure.errors);
        }
        // Success _ `ap` Failure e2 == Failure e2
        else if (first instanceof Success && second instanceof Failure) {
            final Failure<E, A> failure = (Failure<E, A>) second;
            return new Failure<E, B>(failure.errors);
        }
        // Success f `ap` Success a == Success (f a)
        else {
            final Success<E, Function<A, B>> success1 = (Success<E, Function<A, B>>) first;
            final Success<E, A> success2 = (Success<E, A>) second;
            final Function<A, B> f = success1.value;
            final A a = success2.value;
            try {
                return new Success<E, B>(f.apply(a));
            } catch (final Throwable exc) {
                throw new RuntimeException("success ap success failed", exc);
            }
        }
    }


    private static final class Success<E, T> extends Verification<E, T> {
        final T value;

        private Success(@NonNull final T value) {
            this.value = value;
        }

        @Override
        public <R> Verification<E, R> map(@NonNull final Function<? super T, ? extends R> function) {
            //FIXME: I want to reuse rx functions but do not want to add throws to the signature of map
            try {
                return new Success<E, R>(function.apply(value));
            } catch (final Throwable exc) {
                throw new RuntimeException(exc);
            }
        }

        @Override
        public boolean isSuccess() {
            return true;
        }
    }

    private static final class Failure<E, T> extends Verification<E, T> {
        //No need for E to be a Semigroup to simplify things
        final List<E> errors;

        private Failure(@NonNull final List<E> errors) {
            this.errors = errors;
        }

        private Failure(@NonNull final E error) {
            this.errors = Collections.singletonList(error);
        }

        private Failure(@NonNull final E... errors) {
            this.errors = Arrays.asList(errors);
        }

        @Override
        public <R> Verification<E, R> map(@NonNull final Function<? super T, ? extends R> function) {
            return new Failure<E, R>(errors);
        }

        @Override
        public boolean isSuccess() {
            return false;
        }
    }
}
