package com.max.tour.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SPUtils;
import com.max.tour.R;
import com.max.tour.common.MyFragment;
import com.max.tour.constants.Constant;
import com.max.tour.ui.activity.LoginActivity;
import com.max.tour.ui.activity.MainActivity;
import com.max.tour.utils.SpUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Copyright (C) 2019, Relx
 * UserFragment
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-14, ZhengChen, Create file
 */
public class UserFragment extends MyFragment<MainActivity> {


    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }


    @OnClick(R.id.tv_logout)
    public void onViewClicked() {

        SpUtils.clearUserInfo();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
        getActivity().finishAffinity();

    }
}
