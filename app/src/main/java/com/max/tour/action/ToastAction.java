package com.max.tour.action;


import android.support.annotation.StringRes;

import com.blankj.utilcode.util.ToastUtils;


/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/12/08
 *    desc   : 吐司意图
 */
public interface ToastAction {

    default void toast(CharSequence text) {
        ToastUtils.showShort(text);
    }

    default void toast(@StringRes int id) {
        ToastUtils.showShort(id);
    }

}