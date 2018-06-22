package com.loushao.player.network;

import com.loushao.player.Constants;
import com.loushao.player.MyApplication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private static OkHttpClient client=new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder builder=chain.request().newBuilder();
                    builder.addHeader("Authorization","Bearer "+MyApplication.token);
                    return chain.proceed(builder.build());
                }
            }).connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10,TimeUnit.SECONDS)
            .build();
    private static RetrofitService retrofitService=new Retrofit.Builder()
            .baseUrl(Constants.baseUrl())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(RetrofitService.class);
    public static RetrofitService getInstance(){
        return retrofitService;
    }

    private static RetrofitService loginService=new Retrofit.Builder()
            .baseUrl(Constants.baseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(RetrofitService.class);
    public static RetrofitService getLoginService(){
        return loginService;
    }
}
