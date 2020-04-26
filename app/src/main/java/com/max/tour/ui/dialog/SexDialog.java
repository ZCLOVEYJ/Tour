package com.max.tour.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.max.tour.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Copyright (C) 2019, Relx
 * SexDialog
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020/4/26, ZhengChen, Create file
 */
public class SexDialog extends BasePopupWindow implements View.OnClickListener {


    RadioGroup mRadioGroup;
    RadioButton mRadioButtonWoman;
    RadioButton mRadioButtonMan;
    TextView mTvCancel;
    TextView mTvConfirm;

    private Listener listener;

    private int mType = 0;

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    public SexDialog(Context context) {
        super(context);
        mRadioGroup = findViewById(R.id.radio_group);
        mRadioButtonWoman = findViewById(R.id.radio_women);
        mRadioButtonMan = findViewById(R.id.radio_man);
        mTvCancel = findViewById(R.id.tv_cancel);
        mTvConfirm = findViewById(R.id.tv_confirm);
        mTvCancel.setOnClickListener(this::onClick);
        mTvConfirm.setOnClickListener(this::onClick);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_women:
                        mType = 0;
                        break;
                    case R.id.radio_man:
                        mType = 1;
                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.dialog_sex);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                listener.confirm(mType);

                dismiss();
                break;
            default:
        }
    }

    public void setDefaultSex(int i) {
        mType = i;
        if (0 == mType) {
            setRadioCheck(true);
        } else {
            setRadioCheck(false);
        }
    }

    public interface Listener {

        void confirm(int type);
    }

    private void setRadioCheck(boolean check) {
        mRadioButtonWoman.setChecked(check);
        mRadioButtonMan.setChecked(!check);
    }
}
