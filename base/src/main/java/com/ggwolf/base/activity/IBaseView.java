package com.ggwolf.base.activity;

/**
 * 一些数据回调的View处理
 */
public interface IBaseView {
    void showContent();

    void showLoading();

    void onRefreshEmpty();

    void onRefreshFailure(String message);
}
