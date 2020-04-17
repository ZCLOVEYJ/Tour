package com.max.tour.ui.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjq.widget.view.ClearEditText;
import com.hjq.widget.view.PasswordEditText;
import com.max.tour.R;
import com.max.tour.bean.User;
import com.max.tour.common.MyActivity;
import com.max.tour.constants.Constant;
import com.max.tour.helper.DbHelper;
import com.max.tour.helper.InputTextHelper;
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
 * 登录页面
 */
public class LoginActivity extends MyActivity {


    @BindView(R.id.iv_login_logo)
    AppCompatImageView mIvLoginLogo;
    @BindView(R.id.et_login_email)
    ClearEditText mEtLoginEmail;
    @BindView(R.id.et_login_password)
    PasswordEditText mEtLoginPassword;
    @BindView(R.id.tv_login_forget)
    AppCompatTextView mTvLoginForget;
    @BindView(R.id.btn_login_commit)
    AppCompatButton mBtnLoginCommit;
    @BindView(R.id.ll_login_body)
    LinearLayout mLlLoginBody;
    @BindView(R.id.v_login_blank)
    View mVLoginBlank;
    @BindView(R.id.ll_login_other)
    LinearLayout mLlLoginOther;
    @BindView(R.id.tv_login_admin)
    AppCompatTextView mTvLoginAdmin;

    String mEmail;
    String mPassword;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        InputTextHelper.with(this)
                .addView(mEtLoginEmail)
                .addView(mEtLoginPassword)
                .setMain(mBtnLoginCommit)
                .build();
    }

    @Override
    protected void initData() {


    }

    @Override
    public void onRightClick(View v) {
        // 跳转到注册界面
        startActivity(new Intent(this, RegisterActivity.class));
    }


    @OnClick({R.id.btn_login_commit, R.id.tv_login_forget, R.id.tv_login_admin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login_commit:
                mEmail = mEtLoginEmail.getText().toString().trim();
                mPassword = mEtLoginPassword.getText().toString().trim();
                if (TextUtils.isEmpty(mEmail) || !RegexUtils.isEmail(mEmail)) {
                    ToastUtils.showShort("请正确输入邮箱");
                    return;
                }
                if (TextUtils.isEmpty(mPassword)) {
                    ToastUtils.showShort("密码不能为空");
                    return;
                }
                startLogin(mEmail, mPassword);

                break;
            case R.id.tv_login_forget:
                startActivity(new Intent(this, ForgetActivity.class));

                break;
            case R.id.tv_login_admin:
                startActivity(new Intent(this, AdminActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 开始登录
     *
     * @param email
     * @param password
     */
    private void startLogin(String email, String password) {
        Observable
                .create(new ObservableOnSubscribe<HttpData<User>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<User>> emitter) throws Exception {

                        User bean = DbHelper.findUserByEmailPassword(email, StringUtils.encode(password));
                        emitter.onNext(getData(200, "", bean));

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HttpData<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpData<User> data) {
                        if (200 == data.getCode() && data.getData() != null) {
                            // 已发送 跳转到 输入验证码页面
                            jump(data.getData());
                        } else {
                            ToastUtils.showShort("账号密码错误");
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


        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
