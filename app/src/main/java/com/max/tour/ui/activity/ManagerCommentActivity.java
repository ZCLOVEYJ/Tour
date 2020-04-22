package com.max.tour.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.max.tour.R;
import com.max.tour.bean.Comment;
import com.max.tour.common.MyActivity;
import com.max.tour.helper.DataUtils;
import com.max.tour.helper.DbHelper;
import com.max.tour.http.model.HttpData;
import com.max.tour.ui.adapter.ManagerCommentAdapter;

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
 * ManagerUserActivity
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-22, ZhengChen, Create file
 */
public class ManagerCommentActivity extends MyActivity implements BaseQuickAdapter.OnItemChildClickListener {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    List<Comment> mList;
    ManagerCommentAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manager_comment;
    }

    @Override
    protected void initView() {
        mList = new ArrayList<>();
        mAdapter = new ManagerCommentAdapter(mList);
        mAdapter.setOnItemChildClickListener(this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        queryCommentList();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        long mCommentId = mList.get(position).getId();
        deleteCommentWithId(mCommentId);

    }

    private void deleteCommentWithId(long commentId) {

        Observable
                .create(new ObservableOnSubscribe<HttpData<Boolean>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<Boolean>> emitter) throws Exception {

                        boolean result = DbHelper.deleteCommentWithId(commentId);
                        emitter.onNext(DataUtils.getInstance().getData(200, "", result));

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HttpData<Boolean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpData<Boolean> data) {
                        if (200 == data.getCode() && data.getData() != null) {
                            queryCommentList();
                        } else {
                            ToastUtils.showShort("删除评论失败");
                        }
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

    private void queryCommentList() {
        Observable
                .create(new ObservableOnSubscribe<HttpData<List<Comment>>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<List<Comment>>> emitter) throws Exception {

                        List<Comment> list = DbHelper.queryComment();
                        emitter.onNext(DataUtils.getInstance().getData(200, "", list));

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HttpData<List<Comment>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpData<List<Comment>> data) {
                        if (200 == data.getCode() && data.getData() != null) {
                            mList.clear();
                            mList.addAll(data.getData());
                            mAdapter.setNewData(mList);
                        } else {
                            ToastUtils.showShort("获取用户列表失败");
                        }
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
}
