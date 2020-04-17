package com.max.tour.ui.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.hjq.widget.view.PasswordEditText;
import com.max.tour.R;
import com.max.tour.common.MyActivity;
import com.max.tour.helper.DbHelper;
import com.max.tour.http.model.HttpData;
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


public class ForgetNextActivity extends MyActivity {
    @BindView(R.id.et_password_reset_password1)
    PasswordEditText mEtPasswordResetPassword1;
    @BindView(R.id.et_password_reset_password2)
    PasswordEditText mEtPasswordResetPassword2;
    @BindView(R.id.btn_password_reset_commit)
    AppCompatButton mBtnPasswordResetCommit;

    String mEmail;
    String mCode;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_next;
    }

    @Override
    protected void initView() {

        mEmail = getIntent().getStringExtra("email");
        mCode = getIntent().getStringExtra("code");


    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.btn_password_reset_commit)
    public void onViewClicked() {
        if (TextUtils.isEmpty(mEtPasswordResetPassword1.getText().toString()) || TextUtils.isEmpty(mEtPasswordResetPassword2.getText().toString())) {
            toast("密码不能为空");
            return;
        }
        if (!mEtPasswordResetPassword1.getText().toString().equals(mEtPasswordResetPassword2.getText().toString())) {
            toast(" 两次密码不一致");
            return;
        }
        if (TextUtils.isEmpty(mEmail)) {
            toast("账号不能为空");
            return;
        }
        resetPassword(mEtPasswordResetPassword1.getText().toString().trim());


    }

    private void resetPassword(String password) {

        Observable
                .create(new ObservableOnSubscribe<HttpData<String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<String>> emitter) throws Exception {

                        if (DbHelper.updateUser(mEmail, StringUtils.encode(password))) {
                            emitter.onNext(getData(200, "", ""));
                        } else {
                            emitter.onNext(getData(1003, "", ""));
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
                    public void onNext(HttpData<String> stringHttpData) {
                        if (1003 == stringHttpData.getCode()) {
                            // 用户已存在，
                            ToastUtils.showShort("重置密码失败");
                        } else {
                            ToastUtils.showShort("重置密码成功，请重新登录");
                        }
                        startActivity(new Intent(ForgetNextActivity.this, LoginActivity.class));

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

    private HttpData<String> getData(int code, String msg, String str) {
        HttpData<String> data = new HttpData<>();
        data.setCode(code);
        data.setMsg(msg);
        data.setData(str);
        return data;
    }
}
