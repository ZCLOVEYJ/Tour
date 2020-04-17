package com.max.tour.ui.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjq.widget.view.CountdownView;
import com.hjq.widget.view.RegexEditText;
import com.max.tour.R;
import com.max.tour.common.MyActivity;
import com.max.tour.helper.DbHelper;
import com.max.tour.helper.InputTextHelper;
import com.max.tour.http.model.HttpData;
import com.max.tour.utils.EmailUtil;
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
 * ForgetActivity
 */
public class ForgetActivity extends MyActivity {


    @BindView(R.id.et_password_forget_email)
    RegexEditText mEtPasswordForgetEmail;
    @BindView(R.id.et_password_forget_code)
    AppCompatEditText mEtPasswordForgetCode;
    @BindView(R.id.cv_password_forget_countdown)
    CountdownView mCvPasswordForgetCountdown;
    @BindView(R.id.btn_password_forget_commit)
    AppCompatButton mBtnPasswordForgetCommit;

    String mEmail;
    String mEmailTemp;
    String mCode;
    String mCodeTemp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget;
    }

    @Override
    protected void initView() {
        InputTextHelper.with(this)
                .addView(mEtPasswordForgetEmail)
                .addView(mEtPasswordForgetCode)
                .setMain(mBtnPasswordForgetCommit)
                .build();
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.cv_password_forget_countdown, R.id.btn_password_forget_commit})
    public void onViewClicked(View view) {
        mEmail = mEtPasswordForgetEmail.getText().toString().trim();
        switch (view.getId()) {

            case R.id.cv_password_forget_countdown:
                if (!RegexUtils.isEmail(mEmail)) {
                    toast("请正确输入邮箱");
                    return;
                }
                toast(R.string.common_code_send_hint);
                mCvPasswordForgetCountdown.start();
                startSendCode(mEmail);

                break;
            case R.id.btn_password_forget_commit:
                if (!RegexUtils.isEmail(mEmail)) {
                    toast("请正确输入邮箱");
                    return;
                }
                mCode = mEtPasswordForgetCode.getText().toString().trim();
                if (TextUtils.isEmpty(mCode)) {
                    toast("验证码不能为空");
                    return;
                }
                startChange(mEmail, mCode);


                break;
            default:
                break;
        }
    }

    private void startSendCode(String email) {
        Observable
                .create(new ObservableOnSubscribe<HttpData<String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<String>> emitter) throws Exception {

                        if (!DbHelper.findUserByEmail(email)) {
                            emitter.onNext(getData(1001, "用户不存在", ""));
                        } else {
                            // 否则发送邮箱验证码
                            sendEmailCode(email);
                            emitter.onNext(getData(200, "验证码已发送", email));
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
                        if (1001 == stringHttpData.getCode()) {
                            // 用户已存在，
                            ToastUtils.showShort("用户不存在");
                        } else if (200 == stringHttpData.getCode()) {
                            mEmailTemp = stringHttpData.getData();
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

    private void startChange(String email, String code) {

        if (mEmail.equals(mEmailTemp) && mCode.equals(mCodeTemp)) {
            jump(mEmail, mCode);
        } else {
            toast("账号或者验证码错误");
        }


    }

    private void jump(String email, String code) {
        Intent intent = new Intent(this, ForgetNextActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("code", code);
        startActivity(intent);


    }

    /**
     * 发送邮箱验证码
     *
     * @param email
     */
    private void sendEmailCode(String email) {
        mCodeTemp = StringUtils.getCheckCode();
        Observable
                .create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                        EmailUtil.sendMail(email, mCodeTemp);
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io()).subscribe();

    }


    private HttpData<String> getData(int code, String msg, String str) {
        HttpData<String> data = new HttpData<>();
        data.setCode(code);
        data.setMsg(msg);
        data.setData(str);
        return data;
    }
}
