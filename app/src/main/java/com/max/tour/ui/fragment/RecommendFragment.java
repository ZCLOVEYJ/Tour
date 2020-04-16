package com.max.tour.ui.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.max.tour.R;
import com.max.tour.bean.SightsBean;
import com.max.tour.common.MyFragment;
import com.max.tour.helper.DBHelper;
import com.max.tour.http.model.HttpData;
import com.max.tour.ui.activity.MainActivity;
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

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    List<SightsBean> mList;
    GridLayoutManager mLayoutManager;

    RecommendAdapter mAdapter;

    View mEmptyView;

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recomment;
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

        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //数据
                mList.clear();
                getListData();


            }
        });
        getListData();


    }

    private void getListData() {
        Observable
                .create(new ObservableOnSubscribe<HttpData<List<SightsBean>>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<List<SightsBean>>> emitter) throws Exception {

                        List<SightsBean> list = DBHelper.findRecommend();
                        emitter.onNext(getData(200, "", list));

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HttpData<List<SightsBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpData<List<SightsBean>> data) {
                        if (200 == data.getCode() && data.getData() != null) {
                            mList.addAll(data.getData());
                            // 展示 数据
                        }
                        mRefreshLayout.finishRefresh();
                        mAdapter.setNewData(mList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("网络错误");

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    protected void initData() {

    }

    private HttpData<List<SightsBean>> getData(int code, String msg, List<SightsBean> bean) {
        HttpData<List<SightsBean>> data = new HttpData<>();
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

}
