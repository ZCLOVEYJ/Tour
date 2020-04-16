package com.max.tour.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;

import com.max.tour.R;
import com.max.tour.common.MyActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 启动页
 *
 * @author
 */
public class SplashActivity extends MyActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        // 停留一秒 跳转登录页面
        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {

                    startActivity(new Intent(this, MainActivity.class));
                    finish();

                });
    }

    @Override
    protected void initData() {

    }
}
