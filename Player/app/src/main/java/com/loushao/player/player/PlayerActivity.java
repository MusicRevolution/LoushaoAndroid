package com.loushao.player.player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;
import com.loushao.player.MyApplication;
import com.loushao.player.R;
import com.loushao.player.bean.Url;
import com.loushao.player.network.RetrofitFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PlayerActivity extends AppCompatActivity {
    private static final String TAG = "PlayerActivity";
    @BindView(R.id.player)
    MyVideoPlayer player;

    public static void start(Context context, String title, String hash, int size) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("hash", hash);
        intent.putExtra("size", size);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String hash = intent.getStringExtra("hash");
        int size = intent.getIntExtra("size", 0);
        Log.e(TAG, "onCreate: " + title + "\n" + hash + "\n" + size);
        getUrl(hash);
    }
    private void getUrl(String hash){
        Observable<Url> request= RetrofitFactory.getInstance().getUrl(hash);
        request.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, Url>() {
                    @Override
                    public Url apply(Throwable throwable) throws Exception {
                        Log.e(TAG, "apply: " + throwable);
                        return null;
                    }
                })
                .subscribe(new Consumer<Url>() {
                    @Override
                    public void accept(Url url) throws Exception {
                        if (url.getError()==null){
                            Log.e(TAG, "accept111: "+url.getRedirectUrl() );
                            //TODO HttpProxyCacheServer闪退
                            setpath(url.getRedirectUrl());
                        }

                    }
                });
    }
    private void setpath(String url){
        HttpProxyCacheServer proxy=getProxy();
        String proxyUrl=proxy.getProxyUrl(url);
        player.setUp(proxyUrl,MyVideoPlayer.SCREEN_WINDOW_NORMAL);
        player.startVideo();
    }
    private HttpProxyCacheServer getProxy(){
        return MyApplication.getProxy(getApplicationContext());
    }
}
