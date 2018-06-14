package com.example.mediatest.http;

import com.example.mediatest.bean.DownloadUrl;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface RetrofitService {

    @GET("info/{hash}")
    Observable<DownloadUrl> getRedirectUrl(@Header("Authorization") String header, @Path("hash") String hash);
}
