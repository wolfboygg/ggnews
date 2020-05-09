package com.ggwolf.newscenter.newslist;

import com.ggwolf.base.fragment.IBasePagingView;
import com.ggwolf.base.viewmodel.MvvmBaseViewModel;


public class NewsListViewModel extends MvvmBaseViewModel<NewsListViewModel.INewsView, NewsListModel> {



    public interface INewsView extends IBasePagingView {
//        void onNewsLoaded(ArrayList<BaseCustomViewModel> channels);
    }
}
