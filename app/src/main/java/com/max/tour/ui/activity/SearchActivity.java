package com.max.tour.ui.activity;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.max.tour.R;
import com.max.tour.common.MyActivity;
import com.max.tour.event.SearchEvent;
import com.max.tour.ui.adapter.TipAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Copyright (C) 2019, Relx
 * SearchActivity
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-18, ZhengChen, Create file
 */
public class SearchActivity extends MyActivity implements SearchView.OnQueryTextListener,
        Inputtips.InputtipsListener, BaseQuickAdapter.OnItemClickListener {


    @BindView(R.id.searchView)
    SearchView mSearchView;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    List<Tip> mTipList;

    TipAdapter mAdapter;

    LinearLayoutManager mLayoutManager;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {

        ImmersionBar.with(this).statusBarColor(R.color.white).fullScreen(true).autoDarkModeEnable(true);

        initSearchView();

        mTipList = new ArrayList<>();
        mAdapter = new TipAdapter(mTipList);
        mAdapter.setOnItemClickListener(this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initSearchView() {
        mSearchView.setOnQueryTextListener(this);
        //设置SearchView默认为展开显示
        mSearchView.setIconified(false);
        mSearchView.onActionViewExpanded();
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setSubmitButtonEnabled(false);
    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.layout_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public boolean onQueryTextSubmit(String keywords) {

        SearchEvent event = new SearchEvent();
        event.setTag(1);
        event.setKeywords(keywords);
        EventBus.getDefault().post(event);
        this.finish();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        if (!TextUtils.isEmpty(s)) {
            InputtipsQuery inputquery = new InputtipsQuery(s, "北京");
            Inputtips inputTips = new Inputtips(SearchActivity.this.getApplicationContext(), inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        } else {
            if (mAdapter != null && mTipList != null) {
                mTipList.clear();
                mAdapter.notifyDataSetChanged();
            }
        }
        return false;
    }

    @Override
    public void onGetInputtips(List<Tip> list, int rCode) {
        // 正确返回
        if (rCode == 1000) {
            mTipList = list;
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < list.size(); i++) {
                listString.add(list.get(i).getName());
            }
            mAdapter.setNewData(mTipList);

        } else {
            ToastUtils.showShort(rCode + "");
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mTipList != null) {
            Tip tip = mTipList.get(position);

            SearchEvent event = new SearchEvent();
            event.setTag(2);
            event.setKeywords("");
            event.setTip(tip);
            EventBus.getDefault().post(event);
            this.finish();
        }
    }


}
