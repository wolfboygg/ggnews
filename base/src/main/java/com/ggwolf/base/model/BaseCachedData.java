package com.ggwolf.base.model;

import java.io.Serializable;

public class BaseCachedData<T> implements Serializable {
    /***
     * 用来判断是否更新缓存
     */
    public long updateTimeInMills;
    public T data;
}
