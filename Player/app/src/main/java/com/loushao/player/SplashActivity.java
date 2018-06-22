package com.loushao.player;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.loushao.player.login.Login;
import com.loushao.player.login.LoginListener;
import com.loushao.player.login.LoginModel;

public class SplashActivity extends AppCompatActivity implements LoginListener {
    private static final String TAG = "SplashActivity";
    private Database database = new Database(this, "data.db", null, 1);
    private MyApplication app = new MyApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (database.query() == 0) {
            app.setAutoLogin(false);
        } else {
            app.setAutoLogin(true);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (app.isAutoLogin()) {
                    //获取token 直接跳转mainactivity
                    autoLogin();

                } else {
                    startActivity(new Intent(SplashActivity.this, Login.class));
                    finish();
                }

            }
        }, 1000);
    }

    //自动登陆
    public void autoLogin() {
        SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
        String username = pref.getString("username", "");
        String password = pref.getString("password", "");
        //Log.e(TAG, "autoLogin: "+username+"  "+password );
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            LoginModel.getToken(username, password, this);

        } else {
            startActivity(new Intent(SplashActivity.this, Login.class));
            finish();
        }
    }

    @Override
    public void onSuccess(String token) {
        MyApplication app = new MyApplication();
        app.setToken(token);
        //Log.e(TAG, "onSuccess: "+token );
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onFailure(String msg) {
        startActivity(new Intent(SplashActivity.this, Login.class));
        finish();
    }
}
