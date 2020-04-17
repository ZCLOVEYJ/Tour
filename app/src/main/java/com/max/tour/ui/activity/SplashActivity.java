package com.max.tour.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;

import com.blankj.utilcode.util.SPUtils;
import com.max.tour.R;
import com.max.tour.common.MyActivity;
import com.max.tour.constants.Constant;
import com.max.tour.utils.SpUtils;

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
        if (SPUtils.getInstance(Constant.USER).getBoolean("isLogin")){
            SpUtils.initUserInfo();


            // 停留一秒 跳转登录页面
            Observable.timer(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {

                        startActivity(new Intent(this, MainActivity.class));
                        finish();

                    });
        }else {
            // 停留一秒 跳转登录页面
            Observable.timer(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {

                        startActivity(new Intent(this, LoginActivity.class));
                        finish();

                    });
        }


    }

    @Override
    protected void initData() {

    }
}
