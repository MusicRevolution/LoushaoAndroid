package com.example.mediatest.model;

import com.example.mediatest.Constant;
import com.example.mediatest.bean.DownloadUrl;
import com.example.mediatest.http.RetrofitFactory;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GetUrlModel {
    private GetUrlListener listener;
    public void setListener(GetUrlListener listener){
        this.listener=listener;
    }
    public void getUrl(){
        String header="Bearer "+ Constant.token;
        String hash=Constant.hash;
        Observable<DownloadUrl> request= RetrofitFactory.getInstance().getRedirectUrl(header,hash);
        request.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownloadUrl>() {
                    @Override
                    public void accept(DownloadUrl downloadUrl) throws Exception {
                        String downUrl=downloadUrl.getRedirectUrl();
                        listener.onSuccess(downUrl);
                    }
                });
    }
}
