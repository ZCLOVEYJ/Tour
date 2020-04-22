package com.max.tour.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjq.bar.TitleBar;
import com.max.tour.R;
import com.max.tour.bean.Rate;
import com.max.tour.common.MyActivity;
import com.max.tour.event.RatingEvent;
import com.max.tour.helper.DataUtils;
import com.max.tour.helper.DbHelper;
import com.max.tour.http.model.HttpData;
import com.max.tour.ui.adapter.RatingAdapter;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import org.greenrobot.eventbus.EventBus;

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
 * RatingActivity
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-18, ZhengChen, Create file
 */
public class RatingActivity extends MyActivity {


    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    View mHeaderView;
    TextView mTvTitle;
    ScaleRatingBar mRatingBar;

    LinearLayoutManager mLayoutManager;

    RatingAdapter mAdapter;

    List<Rate> mList;

    private long mUserId;
    private long mSightId;

    boolean hasRating = false;

    float ratings = 0f;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_rating;
    }

    @Override
    protected void initView() {

        mUserId = getIntent().getLongExtra("userId", -1);
        mSightId = getIntent().getLongExtra("sightId", -1);


        mList = new ArrayList<>();
        mAdapter = new RatingAdapter(RatingActivity.this, mList);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mHeaderView = View.inflate(this, R.layout.layout_rating_header, null);
        mTvTitle = mHeaderView.findViewById(R.id.tv_title);
        mRatingBar = mHeaderView.findViewById(R.id.ratingBar);
        mRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                ratings = rating;
            }
        });
        mAdapter.addHeaderView(mHeaderView);

        querySightWithUserRating(mUserId, mSightId);
        querySightRating(mSightId);


    }


    private void querySightWithUserRating(long userId, long sightId) {
        Observable
                .create(new ObservableOnSubscribe<HttpData<Rate>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<Rate>> emitter) throws Exception {

                        Rate rate = DbHelper.querySightWithUserRating(userId, sightId);
                        emitter.onNext(DataUtils.getInstance().getData(200, "", rate));

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HttpData<Rate>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpData<Rate> data) {
                        if (200 == data.getCode() && data.getData() != null) {
                            // 已经评分了
                            mTvTitle.setText("您已经评分了");
                            mRatingBar.setRating(data.getData().getScore());
                            mRatingBar.setClearRatingEnabled(false);
                            mRatingBar.setScrollable(false);
                            mRatingBar.setClickable(false);
                            mTitleBar.setRightTitle("");
                            hasRating = true;

                        } else {
                            // 没有评分
                            mTvTitle.setText("您对景区的评分");
                            mRatingBar.setRating(0);
                            mRatingBar.setClearRatingEnabled(true);
                            mRatingBar.setScrollable(true);
                            mRatingBar.setClickable(true);
                            mTitleBar.setRightTitle("提交");
                            hasRating = false;
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

    private void querySightRating(long sightId) {
        Observable
                .create(new ObservableOnSubscribe<HttpData<List<Rate>>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<List<Rate>>> emitter) throws Exception {

                        List<Rate> rates = DbHelper.querySightRating(sightId);
                        emitter.onNext(DataUtils.getInstance().getData(200, "", rates));

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HttpData<List<Rate>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpData<List<Rate>> data) {
                        if (200 == data.getCode() && data.getData() != null) {
                            // 显示评分列表
                            mList.clear();
                            mList.addAll(data.getData());
                            mAdapter.setNewData(mList);


                        } else {
                            ToastUtils.showShort("列表加载失败");
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

    @Override
    protected void initData() {

    }

    @Override
    public void onRightClick(View v) {
        if (!hasRating) {

            if (0f == ratings) {
                ToastUtils.showShort("友情提示，你给景区评分为0");
            }
            commitRating(mSightId, mUserId, ratings);
        }
    }

    private void commitRating(long sightId, long userId, float ratings) {

        Observable
                .create(new ObservableOnSubscribe<HttpData<Boolean>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<Boolean>> emitter) throws Exception {

                        boolean result = DbHelper.commitRating(sightId, userId, ratings);
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

                        if (data.getData()) {
                            ToastUtils.showShort("评分提交成功");
                            // 获取评分列表
                            querySightWithUserRating(mUserId, mSightId);
                            querySightRating(mSightId);

                            RatingEvent event = new RatingEvent();
                            event.setRefresh(true);
                            EventBus.getDefault().post(event);


                        } else {
                            ToastUtils.showShort("评分提交失败");
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
