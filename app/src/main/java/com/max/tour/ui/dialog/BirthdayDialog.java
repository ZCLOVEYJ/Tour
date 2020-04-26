package com.max.tour.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.max.tour.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
public class BirthdayDialog extends BasePopupWindow implements View.OnClickListener {

    private DatePicker mDatePicker;
    private TextView mTvCancel;
    private TextView mTvConfirm;

    private Listener listener;

    private String mBirthday = "";

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public BirthdayDialog(Context context) {
        super(context);
        mDatePicker = findViewById(R.id.datePicker);
        mTvCancel = findViewById(R.id.tv_cancel);
        mTvConfirm = findViewById(R.id.tv_confirm);
        mTvCancel.setOnClickListener(this::onClick);
        mTvConfirm.setOnClickListener(this::onClick);




    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.dialog_birthday);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                listener.confirm(mBirthday);
                dismiss();
                break;
            default:
        }
    }

    public interface Listener {

        void confirm(String str);
    }

    public void setBirthday(String birthday) {
        if (!TextUtils.isEmpty(birthday)){
            mBirthday = birthday;
            String[] str = birthday.split("-");
            int year = Integer.parseInt(str[0]);
            int month = Integer.parseInt(str[1]) - 1;
            int day = Integer.parseInt(str[2]);

            mDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // 获取一个日历对象，并初始化为当前选中的时间
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat format = new SimpleDateFormat(
                            "yyyy-MM-dd");
                    mBirthday = format.format(calendar.getTime());
                    LogUtils.iTag("TAG", mBirthday);
                }
            });
        }else {
            mDatePicker.init(2020, 0, 1, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // 获取一个日历对象，并初始化为当前选中的时间
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat format = new SimpleDateFormat(
                            "yyyy-MM-dd");
                    mBirthday = format.format(calendar.getTime());
                    LogUtils.iTag("TAG", mBirthday);
                }
            });
        }


    }
}
