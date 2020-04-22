package com.max.tour.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.max.tour.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Copyright (C) 2019, Relx
 * InputDialog
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-22, ZhengChen, Create file
 */
public class InputDialog extends BasePopupWindow implements View.OnClickListener {

    EditText mEdName;
    TextView mTvCancel;
    TextView mTvConfirm;

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public InputDialog(Context context) {
        super(context);
        mEdName = findViewById(R.id.et_input);
        mTvCancel = findViewById(R.id.tv_cancel);
        mTvConfirm = findViewById(R.id.tv_confirm);
        mTvCancel.setOnClickListener(this::onClick);
        mTvConfirm.setOnClickListener(this::onClick);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.dialog_input);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                String str = mEdName.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    ToastUtils.showShort("用户名不能为空");
                    return;
                }
                listener.confirm(str);
                dismiss();
                break;
            default:
        }
    }

    public interface Listener {

        void confirm(String str);
    }
}
