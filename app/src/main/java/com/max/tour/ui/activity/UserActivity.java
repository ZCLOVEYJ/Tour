package com.max.tour.ui.activity;

import android.support.v7.widget.AppCompatImageView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjq.widget.layout.SettingBar;
import com.max.tour.R;
import com.max.tour.bean.User;
import com.max.tour.common.MyActivity;
import com.max.tour.constants.Constant;
import com.max.tour.event.UserEvent;
import com.max.tour.helper.DataUtils;
import com.max.tour.helper.DbHelper;
import com.max.tour.http.model.HttpData;
import com.max.tour.ui.dialog.InputDialog;
import com.max.tour.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Copyright (C) 2019, Relx
 * UserActivity
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-14, ZhengChen, Create file
 */
public class UserActivity extends MyActivity {

    @BindView(R.id.iv_person_data_avatar)
    AppCompatImageView mIvPersonDataAvatar;
    @BindView(R.id.fl_person_data_head)
    SettingBar mFlPersonDataHead;
    @BindView(R.id.sb_person_data_id)
    SettingBar mSbPersonDataId;
    @BindView(R.id.sb_person_data_name)
    SettingBar mSbPersonDataName;

    String mName = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.sb_person_data_name)
    public void onViewClicked() {
        InputDialog dialog = new InputDialog(this);
        dialog.setListener(new InputDialog.Listener() {
            @Override
            public void confirm(String str) {
                mName = str;
                updateUserName();
            }
        });
        dialog.showPopupWindow();

    }

    private void updateUserName() {
        Observable
                .create(new ObservableOnSubscribe<HttpData<Boolean>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<Boolean>> emitter) throws Exception {

                        boolean result = DbHelper.updateUser(Constant.mUserId, mName);
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
                        if (200 == data.getCode() && data.getData()) {
                            //
                            mSbPersonDataName.setRightText(mName);
                            Constant.mUserName = mName;
                            SPUtils.getInstance(Constant.USER).put("userName",Constant.mUserName);
                            UserEvent event = new UserEvent();
                            event.setUserName(mName);
                            EventBus.getDefault().post(event);


                        } else {
                            ToastUtils.showShort("修改错误");
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
