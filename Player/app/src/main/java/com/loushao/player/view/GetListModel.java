package com.loushao.player.view;

import android.util.Log;

import com.loushao.player.bean.ResourceList;
import com.loushao.player.network.RetrofitFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class GetListModel {
    private static final String TAG = "GetListModel";
    public static void getList(int page, final ListListener listener) {
        Observable<ResourceList> request = RetrofitFactory.getInstance().getResourceList(page);
        request.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, ResourceList>() {
                    @Override
                    public ResourceList apply(Throwable throwable) throws Exception {
                        Log.e(TAG, "apply: "+throwable );
                        return null;
                    }
                })
                .subscribe(new Consumer<ResourceList>() {
                    @Override
                    public void accept(ResourceList list) throws Exception {
                        //Log.e( "accept: ", ""+list.getData().get(0).getTitle());
                        List<ResourceList> lists = new ArrayList<>();
                        lists.add(list);
                        listener.onSuccess(lists);
                    }
                });
    }
}
