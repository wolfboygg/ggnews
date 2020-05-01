package com.ggwolf.base.model;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.ggwolf.base.preference.BasicDataPreferenceUtil;
import com.ggwolf.base.utils.GsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public abstract class SuperBaseModel<T> {

    protected Handler mUiHandler = new Handler(Looper.getMainLooper());
    /**
     * RxJava来控制消息的传递
     */
    private CompositeDisposable compositeDisposable;
    protected ReferenceQueue<IBaseModelListener> mReferenceQueue;
    protected ConcurrentLinkedQueue<WeakReference<IBaseModelListener>> mWeakListenerArrayList;
    private BaseCachedData<T> mData;

    public SuperBaseModel() {
        mReferenceQueue = new ReferenceQueue<>();
        mWeakListenerArrayList = new ConcurrentLinkedQueue<>();
        if (getCachedPreferenceKey() != null) {
            mData = new BaseCachedData<T>();
        }
    }

    /**
     * 注册监听
     *
     * @param listener
     */
    public void register(IBaseModelListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (this) {
            // 每次注册时候清理已经被系统回收的对象
            Reference<? extends IBaseModelListener> releaseListener = null;
            while ((releaseListener = mReferenceQueue.poll()) != null) {
                mWeakListenerArrayList.remove(releaseListener);
            }

            // 没有回收掉，那么就不进行注册
            for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                IBaseModelListener listenerItem = weakListener.get();
                if (listenerItem != null) {
                    return;
                }
            }
            WeakReference<IBaseModelListener> weakReference = new WeakReference<>(listener, mReferenceQueue);
            mWeakListenerArrayList.add(weakReference);
        }
    }


    /**
     * 取消监听
     *
     * @param listener
     */
    public void unRegister(IBaseModelListener listener) {
        if (listener != null) {
            return;
        }
        synchronized (this) {
            for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                IBaseModelListener listenerItem = weakListener.get();
                if (listener == listenerItem) {
                    mWeakListenerArrayList.remove(weakListener);
                    break;
                }
            }
        }
    }


    /**
     * 由于渠道处在App的首页，为了保证app打开的时候由于网络慢或者异常的情况下tablayout不为空，
     * 所以app对渠道数据进行了预制；
     * 加载完成以后会立即进行网络请求，同时缓存在本地，今后app打开都会从preference读取，而不在读取
     * apk预制数据，由于渠道数据变化没那么快，在app第二次打开的时候会生效，并且是一天请求一次。
     */
    protected void saveDataToPreference(T data) {
        mData.data = data;
        mData.updateTimeInMills = System.currentTimeMillis();
        BasicDataPreferenceUtil.getInstance().setString(getCachedPreferenceKey(), GsonUtils.toJson(mData));
    }

    public abstract void refresh();

    protected abstract void load();

    protected abstract void notifyCachedData(T data);

    /**
     * 该model的数据是否需要缓存，如果需要请返回缓存的key
     */
    protected String getCachedPreferenceKey() {
        return null;
    }

    /**
     * 缓存的数据的类型
     *
     * @return
     */
    protected Type getTClass() {
        return null;
    }

    /**
     * apk级别的缓存数据
     * 该model的数据是否有apk预制的数据，如果有请返回，默认没有
     *
     * @return
     */
    protected String getApkString() {
        return null;
    }

    /**
     * 是否更新数据，可以在这里设计策略，可以是一天一次，一月一次等等，
     * 默认是每次请求都更新
     */
    protected boolean isNeedToUpdate() {
        return true;
    }


    /**
     * 需要将所有的发送的信息取消掉，防止内存泄漏
     */
    public void cancel() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }


    protected interface IBaseModelListener {

    }


    public void addDisposable(Disposable d) {
        if (d == null) {
            return;
        }

        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(d);
    }

    public void getCachedDataAndLoad() {
        if (getCachedPreferenceKey() != null) { // 表示有缓存数据
            String saveDataString = BasicDataPreferenceUtil.getInstance().getString(getCachedPreferenceKey());
            if (!TextUtils.isEmpty(saveDataString)) {
                // 进行转换为对象
                try {
                    T savedData = GsonUtils.fromLocalJson(new JSONObject(saveDataString).getString("data"), getTClass());
                    if (savedData != null) {
                        notifyCachedData(savedData);
                        if (isNeedToUpdate()) {
                            load();
                        }
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (getApkString() != null) {
                notifyCachedData((T) GsonUtils.fromLocalJson(getApkString(), getTClass()));
            }
        }
        load();
    }

}
