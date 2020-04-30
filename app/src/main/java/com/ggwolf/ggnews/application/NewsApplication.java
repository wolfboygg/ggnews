package com.ggwolf.ggnews.application;

import com.ggwolf.base.BaseApplication;
import com.ggwolf.base.BuildConfig;

public class NewsApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // 这里要使用主工程的类型，这样才不会出问题
        setsIsDebug(BuildConfig.DEBUG);
    }
}
