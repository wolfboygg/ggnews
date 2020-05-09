package com.ggwolf.network_api;

import com.ggwolf.network_api.errorhandle.AppDataErrorHandler;
import com.ggwolf.network_api.errorhandle.HttpErrorHandler;
import com.ggwolf.network_api.interceptor.RequestInterceptor;
import com.ggwolf.network_api.interceptor.ResponseInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求的封装类
 */
public abstract class ApiBase {
    protected Retrofit retrofit;
    protected static INetworkRequestInfo networkRequestInfo;
    private static ErrorTransformer sErrorTransformer = new ErrorTransformer();
    private static RequestInterceptor sHttpsRequestInterceptor;
    private static ResponseInterceptor sHttpsResponseInterceptor;

    protected ApiBase(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static void setNetworkRequestInfo(INetworkRequestInfo requestInfo) {
        networkRequestInfo = requestInfo;
        sHttpsRequestInterceptor = new RequestInterceptor(requestInfo);
        sHttpsResponseInterceptor = new ResponseInterceptor();
    }


    public OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS);

        // 添加头信息
        okHttpClient.addInterceptor(sHttpsRequestInterceptor);
        /**
         * 网络请求返回信息处理
         */
        okHttpClient.addInterceptor(sHttpsResponseInterceptor);
        setLoggingLevel(okHttpClient);
        OkHttpClient httpClient = okHttpClient.build();
        httpClient.dispatcher().setMaxRequestsPerHost(20);
        httpClient.newCall(new Request.Builder().build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
        return httpClient;

    }

    public void setLoggingLevel(OkHttpClient.Builder builder) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(networkRequestInfo.isDebug() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        builder.addInterceptor(logging);
    }


    /**
     * 封装线程管理和订阅过程
     *
     * @param observable
     * @param observer
     */
    protected void ApiSubscribe(Observable observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(sErrorTransformer)
                .subscribe(observer);
    }


    /**
     * 处理错误的变换
     * 网络请求的错误处理，其中网络错误分为两类：
     * 1。http请求相关错误，例如404 等等
     * 2。http请求正确，凡事返回的应用数据里提示了异常，表明服务器已经接收到了来自客户端的请求，但是由于
     * * 某些原因，服务器没有正常处理完请求，可能是缺少参数，或者其他原因；
     *
     * @param <T>
     */

    public static class ErrorTransformer<T> implements ObservableTransformer {

        @Override
        public ObservableSource apply(Observable upstream) {
            //onErrorResumeNext当发生错误的时候，由另外一个Observable来代替当前的Observable并继续发射数据
            return (io.reactivex.Observable<T>) upstream.map(new AppDataErrorHandler())
                    .onErrorResumeNext(new HttpErrorHandler<T>());
        }
    }
}
