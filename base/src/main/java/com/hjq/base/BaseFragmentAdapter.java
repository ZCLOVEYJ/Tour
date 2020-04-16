package com.hjq.base;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : FragmentPagerAdapter 基类
 */
public class BaseFragmentAdapter<F extends Fragment> extends FragmentPagerAdapter {

    /**
     * Fragment集合
     */
    private final List<F> mFragmentSet = new ArrayList<>();

    /**
     * 当前显示的Fragment
     */
    private F mCurrentFragment;

    /**
     * 当前 ViewPager
     */
    private ViewPager mViewPager;

    public BaseFragmentAdapter(FragmentActivity activity) {
        super(activity.getSupportFragmentManager());
    }


    @NonNull
    @Override
    public F getItem(int position) {
        return mFragmentSet.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentSet.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (getCurrentFragment() != object) {
            // 记录当前的Fragment对象
            mCurrentFragment = (F) object;
        }
        super.setPrimaryItem(container, position, object);
    }

    public void addFragment(F fragment) {
        mFragmentSet.add(fragment);
    }

    /**
     * 获取Fragment集合
     */
    public List<F> getAllFragment() {
        return mFragmentSet;
    }

    /**
     * 获取当前的Fragment
     */
    public F getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    public void startUpdate(@NonNull ViewGroup container) {
        super.startUpdate(container);
        if (container instanceof ViewPager) {
            // 记录绑定 ViewPager
            mViewPager = (ViewPager) container;
        }
    }

    /**
     * 设置当前条目
     *
     * @param clazz 欲切换的 Fragment
     */
    public void setCurrentItem(Class<? extends F> clazz) {
        for (int i = 0; i < mFragmentSet.size(); i++) {
            if (mFragmentSet.get(i).getClass() == clazz) {
                setCurrentItem(i);
                break;
            }
        }
    }

    public void setCurrentItem(int position) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position);
        }
    }

    public void setCurrentItem(int position, boolean smoothScroll) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position, smoothScroll);
        }
    }
}