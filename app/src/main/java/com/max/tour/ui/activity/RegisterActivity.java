package com.max.tour.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.base.BaseActivity;
import com.max.tour.R;
import com.max.tour.bean.UserBean;
import com.max.tour.common.MyActivity;
import com.max.tour.helper.DBHelper;
import com.max.tour.helper.InputTextHelper;
import com.max.tour.http.model.HttpData;
import com.max.tour.utils.EmailUtil;
import com.max.tour.utils.StringUtils;

import org.litepal.LitePal;

import java.util.List;

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
 * 注册页面
 */
public class RegisterActivity extends MyActivity {


    @BindView(R.id.tv_register_title)
    TextView mTitleView;

    @BindView(R.id.et_register_email)
    EditText mEtEmail;

    @BindView(R.id.et_register_password1)
    EditText mEtPassword1;
    @BindView(R.id.et_register_password2)
    EditText mEtPassword2;

    @BindView(R.id.btn_register_commit)
    Button mBtnCommit;

    String mEmail;
    String mPassword1;
    String mPassword2;

    String mCode;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        // 给这个 View 设置沉浸式，避免状态栏遮挡
        ImmersionBar.setTitleBar(this, mTitleView);

        InputTextHelper.with(this)
                .addView(mEtEmail)
                .addView(mEtPassword1)
                .addView(mEtPassword2)
                .setMain(mBtnCommit)
//                .setListener(helper -> RegexUtils.isEmail(mEtEmail.getText().toString().trim()) &&
//                        mEtPassword1.getText().toString().length() >= 6 &&
//                        mEtPassword2.getText().toString().equals(mEtPassword2.getText().toString()))
                .build();


    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.btn_register_commit)
    public void onViewClicked() {
        mEmail = mEtEmail.getText().toString().trim();
        mPassword1 = mEtPassword1.getText().toString().trim();
        mPassword2 = mEtPassword2.getText().toString().trim();
        if (TextUtils.isEmpty(mEmail) || !RegexUtils.isEmail(mEmail)) {
            ToastUtils.showShort("请正确输入邮箱");
            return;
        }
        if (TextUtils.isEmpty(mPassword1) || TextUtils.isEmpty(mPassword2)) {
            ToastUtils.showShort("密码不能为空");
            return;
        }
        if (!mPassword1.equals(mPassword2)) {
            ToastUtils.showShort("两次密码不一致,请重新输入");
            return;
        }
        startRegister(mEmail, mPassword1);


    }

    /**
     * 开始注册
     */
    @SuppressLint("CheckResult")
    private void startRegister(String email, String password) {
        Observable
                .create(new ObservableOnSubscribe<HttpData<String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<String>> emitter) throws Exception {

                        if (DBHelper.findUserByEmail(email)) {
                            emitter.onNext(getData(1001, "用户已存在", ""));
                        } else {
                            // 否则发送邮箱验证码
                            sendEmailCode(email);
                            emitter.onNext(getData(200, "验证码已发送", ""));
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
                        if (200 == stringHttpData.getCode()) {
                            // 已发送 跳转到 输入验证码页面
                            jump(email, password);


                        } else if (1001 == stringHttpData.getCode()) {
                            // 用户已存在，
                            ToastUtils.showShort("用户已存在，请直接登录");
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("表不存在,用户更不存在");

                        // 否则发送邮箱验证码
                        sendEmailCode(email);

                        // 已发送 跳转到 输入验证码页面
                        jump(email, password);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void jump(String email, String password) {
        Intent intent = new Intent(RegisterActivity.this, RegisterNextActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("code", mCode);
        startActivity(intent);
    }

    /**
     * 发送邮箱验证码
     *
     * @param email
     */
    private void sendEmailCode(String email) {
        mCode = StringUtils.getCheckCode();
        Observable
                .create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                        EmailUtil.sendMail(email, mCode);
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
