package com.example.mediatest;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipelayout)
    SwipeRefreshLayout swipelayout;
    private ListViewAdapter adapter;
    private List<Movie> list = new ArrayList<>();
    String path = Constant.basePath;
    String videoPath = null;
    String danmuPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        ButterKnife.bind(this);
        swipelayout.setOnRefreshListener(this);
        setNavView(navView);
        setheadView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        permissionGranted();
        getData();
        adapter = new ListViewAdapter(this, list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectDanmu(list.get(i).getPath());
            }
        });
    }

    private void setheadView() {
        View view = navView.getHeaderView(0);
        TextView ftpinfo = view.findViewById(R.id.ftpInfo);
        String ip = Utils.getIp(this);
        if (TextUtils.isEmpty(ip)) {
            ftpinfo.setText("获取不到IP，请连接网络");
        } else {
            String str = "ftp://" + ip + ":1234\n" +
                    "账号:user\n" +
                    "密码:123456";
            ftpinfo.setText(str);
        }

    }

    private void getData() {
        if (Utils.isFileExists(path)) {
            Utils.getVideoFile(list, new File(path));
        } else {
            Toast.makeText(this, "创建文件夹失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshData() {
        list.clear();
        Utils.getVideoFile(list, new File(path));
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.report:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.about)
                        .setCancelable(true)
                        .show();
                break;
        }
        return true;
    }

    private void setNavView(NavigationView navView) {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.test:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.test2:
                        drawerLayout.closeDrawers();

                        break;
                    case R.id.test3:
                        drawerLayout.closeDrawers();

                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                refreshData();
                swipelayout.setRefreshing(false);
            }
        });
    }

    private void selectDanmu(String path) {
        videoPath = path;
        new AlertDialog.Builder(this)
                .setTitle("是否加载弹幕")
                .setMessage("是：选择弹幕\n否：直接播放")
                .setCancelable(false)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("text/xml");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(intent, 3);
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                        intent.putExtra("path", videoPath);
                        startActivity(intent);
                    }
                })
                .show();
    }

    @OnClick(R.id.floatingActionButton)
    public void onViewClicked() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            Uri uri = data.getData();
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String img_path = cursor.getString(index);
            cursor.close();
            File file = new File(img_path);
            Movie movie = new Movie();
            movie.setTitle(file.getName());
            movie.setPath(file.getAbsolutePath());
            list.add(movie);
            //Log.e("onActivityResult: ", file.getAbsolutePath());
        }
        if (requestCode == 2) {
            getData();
        }
        if (requestCode == 3) {
            Uri uri = data.getData();
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String img_path = cursor.getString(index);
            cursor.close();
            File file = new File(img_path);
            danmuPath = file.getAbsolutePath();
            Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
            intent.putExtra("path", videoPath);
            intent.putExtra("danmupath", danmuPath);
            startActivity(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
        super.onResume();
    }

    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {//
                // 如果两次按键时间间隔大于2000毫秒，则不退出
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();// 更新mExitTime
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, FtpService.class));
    }

    private void permissionGranted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission_group.STORAGE}, 2);
        } else {

        }
    }
}
