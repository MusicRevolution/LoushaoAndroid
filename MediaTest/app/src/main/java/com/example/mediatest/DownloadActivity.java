package com.example.mediatest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mediatest.http.DownloadManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadActivity extends AppCompatActivity {

    @BindView(R.id.textView3)
    TextView textView3;
    DownloadManager manager = new DownloadManager();
    @BindView(R.id.downList)
    ListView downList;
    static SeekBar seekBar;
    private int maxLength = 94290242;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        seekBar=findViewById(R.id.seekBar);
        ButterKnife.bind(this);
        String path = getIntent().getStringExtra("url");
        if (path != null) {
            textView3.setText(path);
            manager.setBytes(4194304);
            manager.setUrl(path);
            manager.download(DownloadActivity.this);
            seekBar.setMax(94290242);
        } else {
            textView3.setText("url为空");
        }

    }


    public static void updataProgress(int progress) {
        seekBar.setProgress(progress);
    }
}
