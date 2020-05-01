package com.ggwolf.base.fragment;

import com.ggwolf.base.activity.IBaseView;

/**
 * 继承IBaseView 同样也需要进行显示loading什么的页面
 */
public interface IBasePagingView extends IBaseView {

    void onLoadMoreFailure(String message);

    void onLoadMoreEmpty();

}
