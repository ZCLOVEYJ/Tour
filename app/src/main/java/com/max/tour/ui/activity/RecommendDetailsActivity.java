package com.max.tour.ui.activity;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.max.tour.R;
import com.max.tour.bean.RemarkBean;
import com.max.tour.bean.SightsBean;
import com.max.tour.bean.UserBean;
import com.max.tour.common.MyActivity;
import com.max.tour.constants.Constant;
import com.max.tour.helper.DBHelper;
import com.max.tour.helper.DataUtils;
import com.max.tour.http.model.HttpData;
import com.max.tour.ui.adapter.RemarkAdapter;
import com.max.tour.utils.BasicTool;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 景区详情页
 */
public class RecommendDetailsActivity extends MyActivity {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_public_comment)
    TextView mTvPublicComment;
    @BindView(R.id.llyt_public_comment)
    LinearLayout mLlytPublicComment;
    @BindView(R.id.et_public_comment)
    EditText mEtPublicComment;
    @BindView(R.id.tv_public)
    TextView mTvPublic;
    @BindView(R.id.llyt_public_comment_reply)
    LinearLayout mLlytPublicCommentReply;
    @BindView(R.id.rl_layout_bottom)
    RelativeLayout mRlLayoutBottom;


    List<RemarkBean> mList;
    LinearLayoutManager mLayoutManager;
    RemarkAdapter mAdapter;


    View mHeaderView;
    TextView mName;
    ScaleRatingBar mRatingBar;
    TextView mAddress;
    TextView mStatus;

    View mFooterView;
    BGABanner mBanner;


    SightsBean mBean;
    Bundle mBundle;

    /**
     * false = 回复,true = 评论
     */
    private boolean isComment = false;
    /**
     * 评论帖子的时候是0,回复的时候是评论id
     */
    private String isCommentId = "0";

    private int mSightId;
    private int mUserId;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_recommend_details;
    }

    @Override
    protected void initView() {

        mBundle = getIntent().getBundleExtra("value");
        mBean = (SightsBean) mBundle.getSerializable("sight");
        if (mBean != null) {

            initBundleData();

            initHeader();
            initFooter();
            initAdapter();
        }

    }


    private void initBundleData() {
        mSightId = mBean.getId();
        mUserId = Constant.mUserId;

    }

    private void initAdapter() {
        mList = new ArrayList<>();
        mAdapter = new RemarkAdapter(this, mList);
        mAdapter.addHeaderView(mHeaderView);
        mAdapter.addFooterView(mFooterView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始化页面数据
     */
    private void initHeader() {

        mHeaderView = View.inflate(this, R.layout.layout_details_header, null);
        mBanner = mHeaderView.findViewById(R.id.banner);

        mBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(RecommendDetailsActivity.this)
                        .load(model)
                        .placeholder(R.mipmap.bg_default_photo)
                        .error(R.mipmap.bg_default_photo)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });
        mBanner.setData(mBean.getPictures(), mBean.getPictures());

        // name
        mName = mHeaderView.findViewById(R.id.tv_title);
        mRatingBar = mHeaderView.findViewById(R.id.ratingBar);
        mAddress = mHeaderView.findViewById(R.id.tv_address);
        mStatus = mHeaderView.findViewById(R.id.tv_status);


        mName.setText(mBean.getResortName());
        mAddress.setText(mBean.getResortAddress());
        if (mBean.getResortName().contains("暂停营业")) {
            mStatus.setText("暂停营业");
        } else {
            mStatus.setText("正常营业");
        }
        mRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                LogUtils.i("变化了吗-------");
            }
        });
        mRatingBar.setRating(1);
        mRatingBar.setClickable(false);
        mRatingBar.setClearRatingEnabled(false);
        mRatingBar.setScrollable(false);
//        queryRating(mBean.getId());


    }

//    private void queryRating(int id) {
//        Observable
//                .create(new ObservableOnSubscribe<HttpData<Float>>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<HttpData<Float>> emitter) throws Exception {
//
//                        float ratingStr = DBHelper.queryRating(id);
//                        HttpData bean = DataUtils.getInstance().getData(200, "", ratingStr);
//                        emitter.onNext(bean);
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Observer<HttpData<Float>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(HttpData<Float> data) {
//                        if (200 == data.getCode() && data.getData() != null) {
//                            Float rating = data.getData();
//                            mRatingBar.setClearRatingEnabled(true);
//                            mRatingBar.setRating(rating);
//                            LogUtils.i("-------？？/？--------",rating);
//                        }
//
//                        LogUtils.i("-------查询到了吗？--------");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtils.showShort("请求错误");
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

//    private void queryRemark(int id) {
//        Observable
//                .create(new ObservableOnSubscribe<HttpData<List<RemarkBean>>>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<HttpData<List<RemarkBean>>> emitter) throws Exception {
//
//                        List<RemarkBean> list = DBHelper.queryRemarkList(id);
//                        emitter.onNext(bean);
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Observer<HttpData<List<RemarkBean>>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(HttpData<List<RemarkBean>> data) {
//                        if (200 == data.getCode() && data.getData() != null) {
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtils.showShort("请求错误");
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

    private void initFooter() {
        mFooterView = View.inflate(this, R.layout.layout_details_footer, null);
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.tv_public_comment, R.id.llyt_public_comment, R.id.tv_public, R.id.llyt_public_comment_reply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_public_comment:

                mLlytPublicComment.setVisibility(View.GONE);
                mLlytPublicCommentReply.setVisibility(View.VISIBLE);
                mEtPublicComment.setHint("发表你的评论");
                mEtPublicComment.requestFocus();
                mEtPublicComment.callOnClick();
                isComment = true;
                isCommentId = 0 + "";
                BasicTool.showInput(mEtPublicComment);
                break;
            case R.id.llyt_public_comment:
                break;
            case R.id.tv_public:
                if (BasicTool.isEmpty(mEtPublicComment.getText().toString().trim())) {
                    ToastUtils.showShort("评论不能为空");
                    return;
                }
                commitMsg(mEtPublicComment.getText().toString().trim());


                break;
            case R.id.llyt_public_comment_reply:
                break;
            default:
                break;
        }
    }


    private void commitMsg(String msg) {

        Observable
                .create(new ObservableOnSubscribe<HttpData<String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<String>> emitter) throws Exception {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HttpData<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpData<String> data) {
                        if (200 == data.getCode() && data.getData() != null) {

                            // 已发送 跳转到 输入验证码页面
//                            jump(data.getData());
                        } else {
                            ToastUtils.showShort("账号密码错误");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("账号密码错误");

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
