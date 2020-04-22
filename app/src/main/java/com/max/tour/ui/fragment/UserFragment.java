package com.max.tour.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.hjq.widget.layout.SettingBar;
import com.max.tour.R;
import com.max.tour.common.MyFragment;
import com.max.tour.constants.Constant;
import com.max.tour.event.UserEvent;
import com.max.tour.ui.activity.LoginActivity;
import com.max.tour.ui.activity.MainActivity;
import com.max.tour.ui.activity.ManagerCommentActivity;
import com.max.tour.ui.activity.ManagerUserActivity;
import com.max.tour.ui.activity.UserActivity;
import com.max.tour.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
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


    @BindView(R.id.iv_user_icon)
    ImageView mIvUserIcon;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.layout_userInfo)
    LinearLayout mLayoutUserInfo;
    @BindView(R.id.layout_user)
    SettingBar mLayoutUser;
    @BindView(R.id.layout_comment)
    SettingBar mLayoutComment;
    @BindView(R.id.tv_logout)
    TextView mTvLogout;

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        if (!Constant.mIsAdmin){

            mTvUserName.setText(Constant.mUserName);
        }else {
            mTvUserName.setText("admin");
        }

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


    @OnClick({R.id.layout_userInfo, R.id.layout_user, R.id.layout_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_userInfo:
                if (!Constant.mIsAdmin){
                    Intent intent = new Intent(getActivity(), UserActivity.class);
                    startActivity(intent);
                    return;
                }

                break;
            case R.id.layout_user:
                if (!Constant.mIsAdmin){
                    ToastUtils.showShort("你没有管理员权限");
                    return;
                }
                Intent userManager = new Intent(getActivity(), ManagerUserActivity.class);
                startActivity(userManager);

                break;
            case R.id.layout_comment:
                if (!Constant.mIsAdmin){
                    ToastUtils.showShort("你没有管理员权限");
                    return;
                }
                Intent commentManager = new Intent(getActivity(), ManagerCommentActivity.class);
                startActivity(commentManager);
                break;

            default:
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUserInfo(UserEvent event) {
        if (event != null) {
            mTvUserName.setText(event.getUserName());
        }
    }
}
