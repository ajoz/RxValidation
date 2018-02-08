package io.github.ajoz.rxvalidation;

import io.reactivex.annotations.NonNull;

public interface ValidationOperator<Error, Downstream, Upstream> {
    @NonNull
    ValidationObserver<? super Error, ? super Upstream> apply(@NonNull ValidationObserver<? super Error, ? super Downstream> observer) throws Exception;
}
