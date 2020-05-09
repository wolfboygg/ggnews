package com.ggwolf.newscenter.newslist;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ggwolf.base.fragment.MvvmFragment;
import com.ggwolf.base.viewmodel.IMvvmBaseViewModel;
import com.ggwolf.newscenter.R;


/**
 * 新闻列表的Fragment
 */
public class NewsListFragment extends MvvmFragment {


    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public IMvvmBaseViewModel getViewModel() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    protected void onRetryBtnClick() {

    }

    @Override
    protected String getFragmentTag() {
        return null;
    }

}
