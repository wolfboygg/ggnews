package com.ggwolf.base.viewmodel;

import androidx.lifecycle.ViewModel;

import com.ggwolf.base.model.SuperBaseModel;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * V ：Activity Fragment View
 * 这里类中持有这些的引用，要在页面销毁的时候detach掉， 否则会有问题
 *
 * @param <V>
 * @param <M>
 */

public class MvvmBaseViewModel<V, M extends SuperBaseModel> extends ViewModel implements IMvvmBaseViewModel<V> {

    private Reference<V> mUIRef;
    protected M model;

    @Override
    public void attachUI(V view) {
        mUIRef = new WeakReference<>(view);
    }

    /**
     * 获取根View
     *
     * @return
     */
    @Override
    public V getPageView() {
        if (mUIRef == null) {
            return null;
        }
        return mUIRef.get();
    }

    @Override
    public boolean isUIAttached() {
        return mUIRef != null && mUIRef.get() != null;
    }

    @Override
    public void detachUI() {
        if (mUIRef != null) {
            mUIRef.clear();
            mUIRef = null;
        }
        if (model != null) {
            model.cancel();
        }
    }
}
