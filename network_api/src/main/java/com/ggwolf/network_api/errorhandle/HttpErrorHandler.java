package com.ggwolf.network_api.errorhandle;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class HttpErrorHandler<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(Throwable throwable) throws Exception {
        return Observable.error(ExceptionHandle.handleException(throwable));
    }
}
