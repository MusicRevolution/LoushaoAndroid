package com.loushao.player.view;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loushao.player.R;
import com.loushao.player.adapter.ResourceListAdapter;
import com.loushao.player.bean.AnimeDetail;
import com.loushao.player.network.RetrofitFactory;
import com.loushao.player.player.PlayerActivity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DetailFragment extends Fragment {
    private static final String TAG = "DetailFragment";
    @BindView(R.id.img_Big)
    ImageView imgBig;
    @BindView(R.id.txt_Title)
    TextView txtTitle;
    @BindView(R.id.txt_Content)
    TextView txtContent;
    Unbinder unbinder;
    @BindView(R.id.resource_list)
    ListView resourceList;
    @BindView(R.id.play_layout)
    FrameLayout playLayout;

    private List<AnimeDetail.ResourcesBean> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anime_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        int id = (int) getArguments().get("id");
        getData(id);
        return view;
    }

    private void setTxtTitle(String title) {
        txtTitle.setText(title);
    }

    private void setTxtContent(String content) {
        txtContent.setText(content);
    }

    private void setImgBig(String url) {
        Glide.with(this).load(url).into(imgBig);
    }

    private void setList(List<AnimeDetail.ResourcesBean> list) {
        ResourceListAdapter adapter = new ResourceListAdapter(getActivity(), list);
        resourceList.setAdapter(adapter);
    }

    private void setListItemListener() {
        resourceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemClick: " + list.get(i).getUrl());
                PlayerActivity.start(getContext(),list.get(i).getTitle(),list.get(i).getUrl(),list.get(i).getFilesize());
            }
        });
    }

    private void getData(int id) {
        Observable<AnimeDetail> request = RetrofitFactory.getInstance().getAnimeDetail(id);
        request.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, AnimeDetail>() {
                    @Override
                    public AnimeDetail apply(Throwable throwable) throws Exception {
                        Log.e(TAG, "apply: " + throwable);
                        return null;
                    }
                })
                .subscribe(new Consumer<AnimeDetail>() {
                    @Override
                    public void accept(AnimeDetail animeDetail) throws Exception {
                        AnimeDetail.DetailBean data = animeDetail.getDetail();
                        String content = data.getContent();
                        String s = getUrl(data.getContent());
                        content = content.replace(s, "");
                        setTxtTitle(data.getTitle());
                        setTxtContent(content);
                        setImgBig(s);
                        setList(animeDetail.getResources());
                        list = animeDetail.getResources();
                        setListItemListener();
                        //Log.e(TAG, "accept: "+s );
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private String getUrl(String content) {
        String url = null;
        Pattern p = Pattern.compile("\\[img\\](.*)\\[\\/img\\]");
        Matcher m = p.matcher(content);
        while (m.find()) {
            url = m.group(1);
        }
        return url;
    }
}
