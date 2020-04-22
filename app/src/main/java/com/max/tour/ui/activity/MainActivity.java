package com.max.tour.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.max.tour.R;
import com.max.tour.common.MyActivity;
import com.max.tour.event.RefreshEvent;
import com.max.tour.event.RouteEvent;
import com.max.tour.event.TabEntity;
import com.max.tour.helper.ActivityStackManager;
import com.max.tour.helper.DoubleClickHelper;
import com.max.tour.helper.KeyboardWatcher;
import com.max.tour.ui.fragment.HomeFragment;
import com.max.tour.ui.fragment.RecommendFragment;
import com.max.tour.ui.fragment.RouteFragment;
import com.max.tour.ui.fragment.UserFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 主页面
 */
public class MainActivity extends MyActivity implements KeyboardWatcher.SoftKeyboardStateListener {

    @BindView(R.id.vp_home_pager)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout)
    CommonTabLayout mTabLayout;

    private String[] mTitles = {"首页", "推荐", "路线", "我的"};

    private int[] mIconUnselectIds = {
            R.mipmap.tab_ico_home_off, R.mipmap.tab_ico_found_off,
            R.mipmap.tab_ico_message_off, R.mipmap.tab_ico_me_off};
    private int[] mIconSelectIds = {
            R.mipmap.tab_ico_home, R.mipmap.tab_ico_found,
            R.mipmap.tab_ico_message, R.mipmap.tab_ico_me};

    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private MyPagerAdapter mPagerAdapter;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }


    }

    @Override
    protected void initData() {

        mFragments.add(HomeFragment.newInstance());
        mFragments.add(RecommendFragment.newInstance());
        mFragments.add(RouteFragment.newInstance());
        mFragments.add(UserFragment.newInstance());

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mPagerAdapter);

        // 限制页面数量
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());

        mTabLayout.setTabData(mTabEntities);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                RefreshEvent event = new RefreshEvent();
                event.setRefresh(true);
                EventBus.getDefault().post(event);
                if (2 == position) {
                    Intent intent = new Intent(MainActivity.this, RouteActivity.class);
                    startActivity(intent);
                } else {
                    mViewPager.setCurrentItem(position);
                }

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }


    /**
     * {@link KeyboardWatcher.SoftKeyboardStateListener}
     */
    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {

    }

    @Override
    public void onSoftKeyboardClosed() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 回调当前 Fragment 的 onKeyDown 方法
//        if (mPagerAdapter.getCurrentFragment().onKeyDown(keyCode, event)) {
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (DoubleClickHelper.isOnDoubleClick()) {
            // 移动到上一个任务栈，避免侧滑引起的不良反应
            moveTaskToBack(false);
            postDelayed(() -> {

                // 进行内存优化，销毁掉所有的界面
                ActivityStackManager.getInstance().finishAllActivities();
                // 销毁进程（注意：调用此 API 可能导致当前 Activity onDestroy 方法无法正常回调）
                // System.exit(0);

            }, 300);
        } else {
            toast(R.string.home_exit_hint);
        }
    }

    @Override
    protected void onDestroy() {
        mViewPager.setAdapter(null);
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(RouteEvent event) {
        if (event != null) {
            mTabLayout.setCurrentTab(0);
            mViewPager.setCurrentItem(0);
        }
    }


}
