package com.ggwolf.base.model;

import java.lang.ref.WeakReference;

/**
 * 用来分页的Model
 *
 * @param <T>
 */
public abstract class BasePaingModel<T> extends SuperBaseModel<T> {

    protected boolean isRefresh = true;
    protected int pageNumber = 0;


    protected void loadSuccess(T data, final boolean isEmpty, final boolean isFirstPage, final boolean hasNextPage) {
        synchronized (this) {
            mUiHandler.postDelayed(() -> {
                for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                    if (weakListener.get() instanceof IModelListener) {
                        IModelListener listenerItem = (IModelListener) weakListener.get();
                        if (listenerItem != null) {
                            listenerItem.onLoadFinish(BasePaingModel.this, data, isEmpty, isFirstPage, hasNextPage);
                        }
                    }
                }
                // 只缓存第一页的数据
                if (getCachedPreferenceKey() != null && isFirstPage) {
                    saveDataToPreference(data);
                }
            }, 0);
        }
    }

    protected void loadFail(final String prompt, final boolean isFirstPage) {
        synchronized (this) {
            mUiHandler.postDelayed(() -> {
                for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                    if (weakListener.get() instanceof IModelListener) {
                        IModelListener listenerItem = (IModelListener) weakListener.get();
                        if (listenerItem != null) {
                            listenerItem.onLoadFail(BasePaingModel.this, prompt, isFirstPage);
                        }
                    }
                }
            }, 0);
        }

    }


    @Override
    protected void notifyCachedData(T data) {

    }

    public interface IModelListener<T> extends IBaseModelListener {
        void onLoadFinish(BasePaingModel model, T data, boolean ismpty, boolean isFirstPage, boolean hasNextPage);

        void onLoadFail(BasePaingModel model, String prompt, boolean isFirstPage);
    }
}
