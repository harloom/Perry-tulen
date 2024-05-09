package com.ferry.tulen.datasources.listener;

public interface ResultListener<T>
{
    void onSuccess(T result);
    void onError(Throwable error);
}

