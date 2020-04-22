package com.max.tour.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.max.tour.R;
import com.max.tour.bean.Comment;
import com.max.tour.bean.Sights;
import com.max.tour.common.MyActivity;
import com.max.tour.constants.Constant;
import com.max.tour.event.RatingEvent;
import com.max.tour.helper.DataUtils;
import com.max.tour.helper.DbHelper;
import com.max.tour.http.model.HttpData;
import com.max.tour.ui.adapter.CommentAdapter;
import com.max.tour.widget.PopupInput;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
public class SightDetailsActivity extends MyActivity implements BaseQuickAdapter.OnItemChildClickListener {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.layout_comment)
    LinearLayout mLayoutComment;


    List<Comment> mList;
    LinearLayoutManager mLayoutManager;
    CommentAdapter mAdapter;


    View mHeaderView;
    TextView mName;
    TextView mTvScore;
    ScaleRatingBar mRatingBar;
    TextView mAddress;
    TextView mStatus;

    View mFooterView;
    BGABanner mBanner;
    LinearLayout mLayoutRating;


    Sights mBean;
    LatLng mLatLng;

    long mCommentId = -1;

    /**
     * false = 回复,true = 评论
     */
    private boolean isComment = false;
    /**
     * 评论帖子的时候是0,回复的时候是评论id
     */
    private String isCommentId = "0";

    private long mSightId;
    private long mUserId;

    List<String> pictures = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sight_details;
    }

    @Override
    protected void initView() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mLatLng = getIntent().getParcelableExtra("lat_lon");

        querySight(mLatLng);



    }

    private void querySight(LatLng mLatLng) {
        Observable
                .create(new ObservableOnSubscribe<HttpData<Sights>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<Sights>> emitter) throws Exception {

                        Sights sights = DbHelper.findSightByLatLon(mLatLng);
                        emitter.onNext(DataUtils.getInstance().getData(200, "", sights));

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HttpData<Sights>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpData<Sights> data) {
                        if (data.getData() != null) {
                            mBean = data.getData();
                            initBundleData();
                            initHeader();
                            initFooter();
                            initAdapter();

                        } else {
                            ToastUtils.showShort("查找景区失败");
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


    private void initBundleData() {
        mSightId = mBean.getId();
        mUserId = Constant.mUserId;

        for (int i = 0; i < mBean.getPictures().size(); i++) {
            pictures.add(mBean.getPictures().get(i).getPath());
        }

    }

    private void initAdapter() {
        mList = new ArrayList<>();
        mAdapter = new CommentAdapter(this, mList);
        mAdapter.addHeaderView(mHeaderView);
        mAdapter.addFooterView(mFooterView);
        mAdapter.setOnItemChildClickListener(this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        getCommentsList();


    }

    /**
     * 初始化页面数据
     */
    private void initHeader() {

        mHeaderView = View.inflate(this, R.layout.layout_details_header, null);
        mBanner = mHeaderView.findViewById(R.id.banner);
        mLayoutRating = mHeaderView.findViewById(R.id.layout_rating);
        mLayoutRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SightDetailsActivity.this, RatingActivity.class);
                intent.putExtra("userId", mUserId);
                intent.putExtra("sightId", mSightId);
                startActivity(intent);
            }
        });

        mBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(SightDetailsActivity.this)
                        .load(model)
                        .placeholder(R.mipmap.bg_default_photo)
                        .error(R.mipmap.bg_default_photo)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });
        mBanner.setData(pictures, pictures);

        // name
        mName = mHeaderView.findViewById(R.id.tv_title);
        mTvScore = mHeaderView.findViewById(R.id.tv_rating);
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
            }
        });
        mRatingBar.setRating(1);
        mRatingBar.setClickable(false);
        mRatingBar.setClearRatingEnabled(false);
        mRatingBar.setScrollable(false);
        queryRating(mBean.getId());


    }

    private void queryRating(Long id) {

        Observable
                .create(new ObservableOnSubscribe<HttpData<Float>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<Float>> emitter) throws Exception {

                        float score = DbHelper.getAverageRating(mSightId);
                        emitter.onNext(DataUtils.getInstance().getData(200, "", score));

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HttpData<Float>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpData<Float> data) {
                        mRatingBar.setRating(data.getData());
                        mTvScore.setText(data.getData() + "分");
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


    private void initFooter() {
        mFooterView = View.inflate(this, R.layout.layout_details_footer, null);
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.layout_comment,})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.layout_comment:
                showInput();

//                if (BasicTool.isEmpty(mEtPublicComment.getText().toString().trim())) {
//                    ToastUtils.showShort("评论不能为空");
//                    return;
//                }
//

                break;

            default:
                break;
        }
    }

    PopupInput mPopupInput;

    private void showInput() {

        if (mPopupInput == null) {
            mPopupInput = new PopupInput(this).setListener(new PopupInput.SendListener() {
                @Override
                public void onSend(String msg) {
                    if ("1".equals(Constant.mLevel)){
                        ToastUtils.showShort("你没有评论权限,请联系管理员");
                        return ;
                    }
                    commitMsg(msg, mCommentId);
                }
            });
        }
        mPopupInput.setAdjustInputMethod(true)
                .setAutoShowInputMethod(mPopupInput.findViewById(R.id.ed_input), true)
                .showPopupWindow();


    }


    private void commitMsg(String msg, long commentId) {

        Observable
                .create(new ObservableOnSubscribe<HttpData<String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<String>> emitter) throws Exception {
                        if (commentId == -1) {
                            // 提交评论
                            if (DbHelper.saveComment(msg, Constant.mUserId, mSightId)) {

                                emitter.onNext(DataUtils.getInstance().getData(200, "", ""));

                            } else {
                                emitter.onNext(DataUtils.getInstance().getData(1005, "", ""));
                            }
                        } else {
                            // 更新回复
                            if (DbHelper.updateComment(commentId, msg)) {

                                emitter.onNext(DataUtils.getInstance().getData(200, "", ""));

                            } else {
                                emitter.onNext(DataUtils.getInstance().getData(1005, "", ""));
                            }
                        }


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
                            // 查询评论列表
                            getCommentsList();

                        } else {
                            ToastUtils.showShort("信息提交失败");
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

    private void getCommentsList() {

        Observable
                .create(new ObservableOnSubscribe<HttpData<List<Comment>>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<List<Comment>>> emitter) throws Exception {

                        List<Comment> list = DbHelper.getComments(mSightId);
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
                        if (data.getData() != null) {
                            mList.clear();
                            mList.addAll(data.getData());
                            mAdapter.setNewData(mList);
                        } else {
                            ToastUtils.showShort("获取评论列表失败");
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
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Comment comment = mList.get(position);
        mCommentId = comment.getId();
        showInput();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRefresh(RatingEvent event) {
        if (event != null) {
            querySight(mLatLng);
        }
    }


}
