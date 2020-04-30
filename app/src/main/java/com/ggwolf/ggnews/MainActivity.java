package com.ggwolf.ggnews;


import android.os.Bundle;

import com.ggwolf.base.activity.MvvmActivity;
import com.ggwolf.base.viewmodel.MvvmBaseViewModel;
import com.ggwolf.ggnews.databinding.ActivityMainBinding;

/**
 * MainActivity没有数据要绑定，所有它的ViewModel为空即可，然后让子Fragment去处理就可以了
 */

public class MainActivity extends MvvmActivity<ActivityMainBinding, MvvmBaseViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onRetryBtnClick() {

    }

    @Override
    protected MvvmBaseViewModel getViewModel() {
        return null;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}
