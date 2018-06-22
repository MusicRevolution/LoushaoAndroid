package com.loushao.player.network;

import com.loushao.player.bean.AnimeDetail;
import com.loushao.player.bean.ResourceList;
import com.loushao.player.bean.Token;
import com.loushao.player.bean.Url;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService {
    //TOKEN
    @FormUrlEncoded
    @POST("login")
    Observable<Token> getToken(@Field("email") String email, @Field("password") String password);
    //资源列表
    @GET("comics")
    Observable<ResourceList> getResourceList(@Query("page") int page);
    //详细信息
    @FormUrlEncoded
    @POST("comic")
    Observable<AnimeDetail> getAnimeDetail(@Field("id") int id);
    //Url
    @FormUrlEncoded
    @POST("url")
    Observable<Url> getUrl(@Field("hash") String hash);
}
