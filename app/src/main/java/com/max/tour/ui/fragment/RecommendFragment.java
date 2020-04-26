package com.max.tour.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.hjq.widget.layout.NoScrollViewPager;
import com.max.tour.R;
import com.max.tour.common.MyFragment;
import com.max.tour.event.TabEntity;
import com.max.tour.ui.activity.MainActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Copyright (C) 2019, Relx
 * RecommendFragment
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-14, ZhengChen, Create file
 */
public class RecommendFragment extends MyFragment<MainActivity> {


    @BindView(R.id.tabLayout)
    CommonTabLayout mTabLayout;
    @BindView(R.id.viewPager)
    NoScrollViewPager mViewPager;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntity = new ArrayList<>();
    private String[] mTitles = {"全部", "评分", "热门"};

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recomment;
    }

    @Override
    protected void initView() {
        for (int i = 0; i < mTitles.length; i++) {

            SightListFragment fragment = new SightListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("tag", i);
            fragment.setArguments(bundle);
            mFragments.add(fragment);
            mTabEntity.add(new TabEntity(mTitles[i], 0, 0));
        }

        mViewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragments.get(i);
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }
        });

        mTabLayout.setTabData(mTabEntity);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mTabLayout.setCurrentTab(0);



    }



    @Override
    protected void initData() {

    }


    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
}
