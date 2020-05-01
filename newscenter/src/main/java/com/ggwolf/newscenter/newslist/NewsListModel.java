package com.ggwolf.newscenter.newslist;

import com.ggwolf.base.model.BasePaingModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NewsListModel<T> extends BasePaingModel<T> {

    private String mChannelId = "";
    private String mChannelName = "";

    private static final String PREF_KEY_NEWS_CHANNEL = "pref_key_news_";

    @Override
    protected String getCachedPreferenceKey() {
        return PREF_KEY_NEWS_CHANNEL + mChannelId;
    }

    @Override
    protected Type getTClass() {
        return new TypeToken<ArrayList<>>
    }

    public NewsListModel(String mChannelId, String mChannelName) {
        this.mChannelId = mChannelId;
        this.mChannelName = mChannelName;
    }

    @Override
    public void refresh() {
        isRefresh = true;
        load();
    }

    public void loadNextPage() {
        isRefresh = false;
        load();
    }

    @Override
    protected void load() {

    }
}
