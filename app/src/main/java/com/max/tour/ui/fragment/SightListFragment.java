package com.max.tour.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amap.api.maps2d.model.LatLng;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.max.tour.R;
import com.max.tour.bean.Sights;
import com.max.tour.common.MyFragment;
import com.max.tour.helper.DbHelper;
import com.max.tour.http.model.HttpData;
import com.max.tour.ui.activity.MainActivity;
import com.max.tour.ui.activity.SightDetailsActivity;
import com.max.tour.ui.adapter.RecommendAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Copyright (C) 2019, Relx
 * SightListFragment
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020/4/26, ZhengChen, Create file
 */
public class SightListFragment extends MyFragment<MainActivity> implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    List<Sights> mList;
    GridLayoutManager mLayoutManager;

    RecommendAdapter mAdapter;

    View mEmptyView;

    int tag = 0;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sight_list;
    }

    @Override
    protected void initView() {

        mList = new ArrayList<>();
        mAdapter = new RecommendAdapter(getActivity(), mList);

        mEmptyView = View.inflate(getActivity(), R.layout.layout_empty, null);

        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setEmptyView(mEmptyView);
        mAdapter.setOnItemClickListener(this);

        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //数据
                mList.clear();
                if (0 == tag) {
                    getListData();
                } else if (1 == tag) {
                    getListDataWithRating();
                } else if (2 == tag) {
                    getListDataWithHot();
                }


            }
        });


    }

    @Override
    protected void initData() {
        LogUtils.iTag("TAG","initData = "+tag);
        if (0 == tag) {
            getListData();
        } else if (1 == tag) {
            getListDataWithRating();
        } else if (2 == tag) {
            getListDataWithHot();
        }
    }

    private void getListDataWithHot() {
        Observable
                .create(new ObservableOnSubscribe<HttpData<List<Sights>>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<List<Sights>>> emitter) throws Exception {

                        List<Sights> list = DbHelper.findRecommendWithHot();
                        emitter.onNext(getData(200, "", list));

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HttpData<List<Sights>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpData<List<Sights>> data) {
                        if (200 == data.getCode() && data.getData() != null) {
                            mList.addAll(data.getData());
                            // 展示 数据
                        }
                        mRefreshLayout.finishRefresh();
                        mAdapter.setNewData(mList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void getListDataWithRating() {
        Observable
                .create(new ObservableOnSubscribe<HttpData<List<Sights>>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<List<Sights>>> emitter) throws Exception {

                        List<Sights> list = DbHelper.findRecommendWithRating();
                        emitter.onNext(getData(200, "", list));

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HttpData<List<Sights>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpData<List<Sights>> data) {
                        if (200 == data.getCode() && data.getData() != null) {
                            mList.addAll(data.getData());
                            // 展示 数据
                        }
                        mRefreshLayout.finishRefresh();
                        mAdapter.setNewData(mList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }


    private void getListData() {
        Observable
                .create(new ObservableOnSubscribe<HttpData<List<Sights>>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<List<Sights>>> emitter) throws Exception {

                        List<Sights> list = DbHelper.findRecommend();
                        emitter.onNext(getData(200, "", list));

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HttpData<List<Sights>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpData<List<Sights>> data) {
                        if (200 == data.getCode() && data.getData() != null) {
                            mList.addAll(data.getData());
                            // 展示 数据
                        }
                        mRefreshLayout.finishRefresh();
                        mAdapter.setNewData(mList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (args != null) {
            tag = args.getInt("tag");
            LogUtils.iTag("TAG","tag = "+tag);
        }

    }



    private HttpData<List<Sights>> getData(int code, String msg, List<Sights> bean) {
        HttpData<List<Sights>> data = new HttpData<>();
        data.setCode(code);
        data.setMsg(msg);
        data.setData(bean);
        return data;
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getActivity(), SightDetailsActivity.class);
        LatLng latLng = new LatLng(mList.get(position).getLatitude(), mList.get(position).getLongitude());
        intent.putExtra("lat_lon", latLng);
        getActivity().startActivity(intent);
    }
}
