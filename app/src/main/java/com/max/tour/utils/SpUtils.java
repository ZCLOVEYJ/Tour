package com.max.tour.utils;

import com.blankj.utilcode.util.SPUtils;
import com.max.tour.constants.Constant;

/**
 * Copyright (C) 2019, Relx
 * SpUtils
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-16, ZhengChen, Create file
 */
public class SpUtils {

    public static void savaUserInfo() {

        SPUtils.getInstance(Constant.USER).put("userId", Constant.mUserId);
        SPUtils.getInstance(Constant.USER).put("userName", Constant.mUserName);
        SPUtils.getInstance(Constant.USER).put("name", Constant.mName);
        SPUtils.getInstance(Constant.USER).put("sex", Constant.mSex);
        SPUtils.getInstance(Constant.USER).put("date", Constant.mDate);
        SPUtils.getInstance(Constant.USER).put("email", Constant.mEmail);
        SPUtils.getInstance(Constant.USER).put("userIcon", Constant.mUserIcon);
        SPUtils.getInstance(Constant.USER).put("info", Constant.mInfo);
        SPUtils.getInstance(Constant.USER).put("isAdmin", Constant.mIsAdmin);
        SPUtils.getInstance(Constant.USER).put("adminName", Constant.mAdminName);
        SPUtils.getInstance(Constant.USER).put("adminId", Constant.mAdminId);
        SPUtils.getInstance(Constant.USER).put("level", Constant.mLevel);
        SPUtils.getInstance(Constant.USER).put("isLogin", true);


    }

    public static void clearUserInfo() {
        SPUtils.getInstance(Constant.USER).put("userId", "");
        SPUtils.getInstance(Constant.USER).put("userName", "");
        SPUtils.getInstance(Constant.USER).put("name", "");
        SPUtils.getInstance(Constant.USER).put("sex", "");
        SPUtils.getInstance(Constant.USER).put("date", "");
        SPUtils.getInstance(Constant.USER).put("email", "");
        SPUtils.getInstance(Constant.USER).put("userIcon", "");
        SPUtils.getInstance(Constant.USER).put("info", "");
        SPUtils.getInstance(Constant.USER).put("isAdmin", false);
        SPUtils.getInstance(Constant.USER).put("adminName", "");
        SPUtils.getInstance(Constant.USER).put("adminId", "");
        SPUtils.getInstance(Constant.USER).put("isLogin", false);
        SPUtils.getInstance(Constant.USER).put("level", "");
    }

    public static void initUserInfo() {

        Constant.mUserId = SPUtils.getInstance(Constant.USER).getLong("userId");
        Constant.mUserName = SPUtils.getInstance(Constant.USER).getString("userName", "");
        Constant.mName = SPUtils.getInstance(Constant.USER).getString("name", "");
        Constant.mSex = SPUtils.getInstance(Constant.USER).getString("sex", "");
        Constant.mDate = SPUtils.getInstance(Constant.USER).getString("date", "");
        Constant.mEmail = SPUtils.getInstance(Constant.USER).getString("email", "");
        Constant.mUserIcon = SPUtils.getInstance(Constant.USER).getString("userIcon", "");
        Constant.mInfo = SPUtils.getInstance(Constant.USER).getString("info", "");
        Constant.mIsAdmin = SPUtils.getInstance(Constant.USER).getBoolean("isAdmin", false);
        Constant.mAdminName = SPUtils.getInstance(Constant.USER).getString("adminName", "");
        Constant.mAdminId = SPUtils.getInstance(Constant.USER).getLong("adminId");
        Constant.mLevel = SPUtils.getInstance(Constant.USER).getString("level","");
    }
}
