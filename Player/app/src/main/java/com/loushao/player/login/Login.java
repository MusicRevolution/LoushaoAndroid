package com.loushao.player.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loushao.player.Database;
import com.loushao.player.MainActivity;
import com.loushao.player.MyApplication;
import com.loushao.player.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity implements LoginListener {
    private static final String TAG = "Login";
    Database database = new Database(this, "data.db", null, 1);
    @BindView(R.id.edt_Account)
    EditText edtAccount;
    @BindView(R.id.edt_Password)
    EditText edtPassword;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }


    private void saveAccount(String username, String password) {
        SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_PRIVATE).edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
        database.updata(1);
    }

    @OnClick({R.id.btn_Login, R.id.btn_Register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_Login:
                username = edtAccount.getText().toString();
                password = edtPassword.getText().toString();
                LoginModel.getToken(username, password, this);
                break;
            case R.id.btn_Register:
                break;
        }
    }

    @Override
    public void onSuccess(String token) {
        MyApplication app = new MyApplication();
        app.setToken(token);
        //Log.e(TAG, "onSuccess: "+token );
        saveAccount(username, password);
        startActivity(new Intent(Login.this, MainActivity.class));
        finish();
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
