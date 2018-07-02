package com.loushao.player.login;

import android.util.Log;

import com.loushao.player.bean.Token;
import com.loushao.player.network.RetrofitFactory;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LoginModel {
    public static void getToken(String email, String password, final LoginListener listener) {
        Observable<Token> request = RetrofitFactory.getLoginService().getToken(email, password);
        request.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, Token>() {
                    @Override
                    public Token apply(Throwable throwable) throws Exception {
                        Log.e( "login fail: ",""+throwable );
                        return null;
                    }
                })
                .subscribe(new Consumer<Token>() {
                    @Override
                    public void accept(Token token) throws Exception {
                        String t = token.getResult();
                        String fail = "邮箱或密码错误.";
                        if (t.equals(fail)) {
                            listener.onFailure(fail);
                        } else {
                            listener.onSuccess(t);
                        }
                    }
                });
    }
}
