package com.example.mediatest.netplaytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.danikula.videocache.HttpProxyCacheServer;
import com.example.mediatest.PlayerActivity;
import com.example.mediatest.R;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        HttpProxyCacheServer proxy = getProxy();
        String proxyUrl = proxy.getProxyUrl("https://www.hi1994.com/lbxx/1.mp4");
        Intent intent=new Intent(TestActivity.this, PlayerActivity.class);
        intent.putExtra("path", proxyUrl);
        startActivity(intent);
    }
    private HttpProxyCacheServer getProxy() {
        // should return single instance of HttpProxyCacheServer shared for whole app.
        return App.getProxy(getApplicationContext());
    }
    @OnClick(R.id.button)
    public void onViewClicked() {
        Intent intent = new Intent(TestActivity.this, CaptureActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                //String proxyUrl = proxy.getProxyUrl(content);
                //Intent intent=new Intent(TestActivity.this, PlayerActivity.class);
                //intent.putExtra("path", proxyUrl);
                //startActivity(intent);
                //Toast.makeText(this,"扫描结果为：" + content,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
