package com.loushao.player;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loushao.player.adapter.ListViewAdapter;
import com.loushao.player.bean.ResourceList;
import com.loushao.player.login.Login;
import com.loushao.player.view.DetailFragment;
import com.loushao.player.view.GetListModel;
import com.loushao.player.view.ListListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ListListener {
    private static final String TAG = "MainActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipelayout)
    SwipeRefreshLayout swipelayout;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    Database database = new Database(this, "data.db", null, 1);
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.detail)
    FrameLayout detail;

    private List<ResourceList.DataBean> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        ButterKnife.bind(this);
        setToolbar();
        setNavView(navView);
        showProgress();
        getData();
        setOnClickListener();
    }

    private void setOnClickListener() {

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id = lists.get(i).getId();
                //Log.e(TAG, "onItemClick: "+id );
                openFragment(id);
            }
        });
        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private void openFragment(int id) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.detail, fragment);
        transaction.addToBackStack(null);//返回按钮只退出fragment
        transaction.commit();
    }

    private void getData() {
        GetListModel.getList(1, this);
    }


    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setNavView(NavigationView navView) {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.test:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.test2:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.test3:
                        drawerLayout.closeDrawers();
                        logout();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    public void logout() {
        SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_PRIVATE).edit();
        editor.putString("username", "");
        editor.putString("password", "");
        editor.apply();
        database.updata(0);
        startActivity(new Intent(MainActivity.this, Login.class));
        finish();
    }

    @Override
    public void onSuccess(List<ResourceList> list) {
        lists = new ArrayList<>();
        for (int i = 0; i < list.get(0).getData().size(); i++) {
            ResourceList.DataBean dataBean = list.get(0).getData().get(i);
            lists.add(dataBean);
        }
        hideProgress();
        ListViewAdapter adapter = new ListViewAdapter(MainActivity.this, lists);
        listview.setAdapter(adapter);
    }

    @Override
    public void onFailure() {
        hideProgress();
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        swipelayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }
/*
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
    */
}
