package com.ggwolf.base.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.ggwolf.base.loadsir.EmptyCallback;
import com.ggwolf.base.loadsir.ErrorCallback;
import com.ggwolf.base.loadsir.LoadingCallback;
import com.ggwolf.base.viewmodel.IMvvmBaseViewModel;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

/**
 * 这里要指定成abstract，让子类必须传入layout布局
 *
 *
 *
 */
public abstract class MvvmActivity<V extends ViewDataBinding, VM extends IMvvmBaseViewModel> extends AppCompatActivity implements IBaseView {

    protected VM viewModel;
    // loadSir 一个用来处理页面View替换的三方库
    private LoadService mLoadService;
    protected V viewDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
        performDataBinding();
    }

    private void initViewModel() {
        viewModel = getViewModel();
        if (viewModel != null) {
            viewModel.attachUI(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null && viewModel.isUIAttached()) {
            viewModel.detachUI();
        }
    }

    @Override
    public void onRefreshEmpty() {
        if (mLoadService != null) {
            mLoadService.showCallback(EmptyCallback.class);
        }
    }


    @Override
    public void onRefreshFailure(String message) {
        if (mLoadService != null) {
            mLoadService.showCallback(ErrorCallback.class);
        }
    }

    @Override
    public void showLoading() {
        if (mLoadService != null) {
            mLoadService.showCallback(LoadingCallback.class);
        }
    }

    @Override
    public void showContent() {
        if (mLoadService != null) {
            mLoadService.showSuccess();
        }
    }

    public void setLoadSir(View view) {
        // You can change the callback on sub thread directly.
        mLoadService = LoadSir.getDefault().register(view, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                onRetryBtnClick();
            }
        });
    }

    protected abstract void onRetryBtnClick();

    protected abstract VM getViewModel();

    public abstract int getBindingVariable();

    public abstract @LayoutRes
    int getLayoutId();

    /**
     * 真正绑定数据的地方
     */
    private void performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        viewModel = viewModel == null ? getViewModel() : viewModel;
        if (getBindingVariable() > 0) {// 绑定有数据，那么需要进行处理
            viewDataBinding.setVariable(getBindingVariable(), viewModel);
        }
        viewDataBinding.executePendingBindings();
    }
}
