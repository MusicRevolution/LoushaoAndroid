package com.example.mediatest.http;

import com.example.mediatest.bean.DownloadUrl;
import com.example.mediatest.bean.Metadata;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface RetrofitService {

    @GET("info/{hash}")
    Observable<DownloadUrl> getRedirectUrl(@Header("Authorization") String header, @Path("hash") String hash);
    @GET("metadata/{hash}")
    Observable<Metadata> getMetadata(@Path("hash") String hash);
}
