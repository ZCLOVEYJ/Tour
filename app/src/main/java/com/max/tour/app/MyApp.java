package com.max.tour.app;

import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.hjq.http.EasyConfig;
import com.hjq.http.config.IRequestServer;
import com.max.tour.helper.ActivityStackManager;
import com.max.tour.helper.DBHelper;
import com.max.tour.http.model.RequestHandler;
import com.max.tour.http.server.ReleaseServer;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import okhttp3.OkHttpClient;

/**
 * Copyright (C) 2019, Relx
 * MyApp
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-13, ZhengChen, Create file
 */
public class MyApp extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // 主要是添加下面这句代码
        MultiDex.install(this);

        // Activity 栈管理初始化
        ActivityStackManager.getInstance().init(this);

        // 网络请求框架初始化
        IRequestServer server = new ReleaseServer();


        EasyConfig.with(new OkHttpClient())
                // 是否打印日志
                .setLogEnabled(true)
                // 设置服务器配置
                .setServer(server)
                // 设置请求处理策略
                .setHandler(new RequestHandler())
                // 设置请求重试次数
                .setRetryCount(3)
                // 添加全局请求参数
                //.addParam("token", "6666666")
                // 添加全局请求头
                //.addHeader("time", "20191030")
                // 启用配置
                .into();

        // 初始化表
        LitePal.getDatabase();
        // 调试工具集成
        Stetho.initializeWithDefaults(this);
        // 初始化一个Admin
        DBHelper.initAdmin();




    }
}
