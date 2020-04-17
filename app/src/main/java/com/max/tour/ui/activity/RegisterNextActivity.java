package com.max.tour.ui.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.hjq.widget.view.RegexEditText;
import com.max.tour.R;
import com.max.tour.bean.User;
import com.max.tour.common.MyActivity;
import com.max.tour.constants.Constant;
import com.max.tour.helper.DbHelper;
import com.max.tour.http.model.HttpData;
import com.max.tour.utils.SpUtils;
import com.max.tour.utils.StringUtils;

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
 * 注册的下一个页面
 */
public class RegisterNextActivity extends MyActivity {

    @BindView(R.id.tv_register_title)
    AppCompatTextView mTvTitle;

    @BindView(R.id.et_register_code)
    RegexEditText mEtCode;

    String mEmail;
    String mPassword;
    String mCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_next;
    }

    @Override
    protected void initView() {
        mEmail = getIntent().getStringExtra("email");
        mPassword = getIntent().getStringExtra("password");
        mCode = getIntent().getStringExtra("code");

        mTvTitle.setText("验证码已发送至" + mEmail + ",请注意查收");


    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.btn_register_commit)
    public void onViewClicked() {
        String code = mEtCode.getText().toString().trim();
        if (TextUtils.isEmpty(code) || !mCode.equals(code)) {
            ToastUtils.showShort("验证码不正确,请重新输入");
            return;
        }
        startRegister();


    }

    /**
     * 开始注册
     */
    private void startRegister() {
        Observable
                .create(new ObservableOnSubscribe<HttpData<User>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<User>> emitter) throws Exception {

                        User user = DbHelper.insertUser(mEmail, StringUtils.encode(mPassword));

                        emitter.onNext(getData(200, "", user));

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HttpData<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpData<User> bean) {
                        if (200 == bean.getCode() && bean.getData() != null) {
                            jump(bean.getData());

                        } else {
                            ToastUtils.showShort("注册失败");
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

    private void jump(User bean) {
        Constant.mUserId = bean.getId();
        Constant.mUserName = bean.getUserName();
        Constant.mName = bean.getName();
        Constant.mSex = bean.getSex();
        Constant.mDate = bean.getBirthday();
        Constant.mEmail = bean.getEmail();
        Constant.mUserIcon = bean.getUserIcon();
        Constant.mInfo = bean.getInfo();
        Constant.mIsAdmin = false;

        // 保存用户信息
        SpUtils.savaUserInfo();


        Intent intent = new Intent(RegisterNextActivity.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private HttpData<User> getData(int code, String msg, User bean) {
        HttpData<User> data = new HttpData<>();
        data.setCode(code);
        data.setMsg(msg);
        data.setData(bean);
        return data;
    }
}
