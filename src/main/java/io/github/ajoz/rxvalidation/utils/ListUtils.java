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

package io.github.ajoz.rxvalidation.utils;

import io.reactivex.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ListUtils {
    private ListUtils() {
    }

    @NonNull
    public static <T> List<T> union(@NonNull final List<? extends T> first,
                                    @NonNull final List<? extends T> second) {
        final List<T> result = new ArrayList<T>(first);
        result.addAll(second);
        return Collections.unmodifiableList(result);
    }
}
