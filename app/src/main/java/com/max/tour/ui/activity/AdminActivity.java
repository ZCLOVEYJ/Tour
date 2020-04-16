package com.max.tour.ui.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.hjq.widget.view.ClearEditText;
import com.hjq.widget.view.PasswordEditText;
import com.max.tour.R;
import com.max.tour.bean.AdministratorBean;
import com.max.tour.common.MyActivity;
import com.max.tour.constants.Constant;
import com.max.tour.helper.DBHelper;
import com.max.tour.helper.InputTextHelper;
import com.max.tour.http.model.HttpData;

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
 * 管理员登录
 */
public class AdminActivity extends MyActivity {
    @BindView(R.id.et_login_name)
    ClearEditText mEtLoginName;
    @BindView(R.id.et_login_password)
    PasswordEditText mEtLoginPassword;
    @BindView(R.id.btn_login_commit)
    AppCompatButton mBtnLoginCommit;


    String mName;
    String mPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_admin;
    }

    @Override
    protected void initView() {
        InputTextHelper.with(this)
                .addView(mEtLoginName)
                .addView(mEtLoginPassword)
                .setMain(mBtnLoginCommit)
                .build();


    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.btn_login_commit)
    public void onViewClicked() {
        mName = mEtLoginName.getText().toString().trim();
        mPassword = mEtLoginPassword.getText().toString().trim();
        if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mPassword)) {
            ToastUtils.showShort("账号密码不能为空");
            return;
        }
        startAdminLogin(mName, mPassword);

    }

    private void startAdminLogin(String name, String password) {
        Observable
                .create(new ObservableOnSubscribe<HttpData<AdministratorBean>>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpData<AdministratorBean>> emitter) throws Exception {

                        AdministratorBean bean = DBHelper.findAdminByNamePassword(name, password);
                        emitter.onNext(getData(200, "", bean));

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HttpData<AdministratorBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpData<AdministratorBean> data) {
                        if (200 == data.getCode() && data.getData() != null) {

                            // 已发送 跳转到 输入验证码页面
                            jump(data.getData());
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

    private void jump(AdministratorBean data) {

        Constant.mIsAdmin = true;
        Constant.mAdminName = data.getAdminname();
        Constant.mAdminId = data.getId();

        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();

    }

    private HttpData<AdministratorBean> getData(int code, String msg, AdministratorBean bean) {
        HttpData<AdministratorBean> data = new HttpData<>();
        data.setCode(code);
        data.setMsg(msg);
        data.setData(bean);
        return data;
    }
}
