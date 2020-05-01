package com.ggwolf.base.model;

import java.lang.ref.WeakReference;

public abstract class BaseModel<T> extends SuperBaseModel<T> {

    /**
     * 加载网络数据成功
     * 通知所有的注册加载结果
     *
     * @param data
     */
    protected void loadSuccess(T data) {
        synchronized (this) {
            mUiHandler.postDelayed(() -> {
                for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                    if (weakListener.get() instanceof IModelListener) {
                        IModelListener listenerItem = (IModelListener) weakListener.get();
                        if (listenerItem != null) {
                            listenerItem.onLoadFinish(BaseModel.this, data);
                        }
                    }
                }
                /** 如果我们需要缓存数据，加载成功，让我们保存他到preference */
                if (getCachedPreferenceKey() != null) {
                    saveDataToPreference(data);
                }
            }, 0);
        }
    }

    /**
     * 加载网络数据失败
     * 通知所有的注册着加载结果
     */
    protected void loadFail(final String prompt) {
        synchronized (this) {
            mUiHandler.postDelayed(() -> {
                for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                    if (weakListener.get() instanceof IModelListener) {
                        IModelListener listenerItem = (IModelListener) weakListener.get();
                        if (listenerItem != null) {
                            listenerItem.onLoadFail(BaseModel.this, prompt);
                        }
                    }
                }
            }, 0);
        }
    }


    public interface IModelListener<T> extends IBaseModelListener {
        void onLoadFinish(BaseModel baseModel, T data);

        void onLoadFail(BaseModel baseModel, String prompt);
    }

}
