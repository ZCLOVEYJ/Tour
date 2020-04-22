package com.max.tour.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.max.tour.R;
import com.max.tour.bean.User;

import java.util.List;

/**
 * Copyright (C) 2019, Relx
 * ManagerUserAdapter
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-22, ZhengChen, Create file
 */
public class ManagerUserAdapter extends BaseQuickAdapter<User, BaseViewHolder> {

    public ManagerUserAdapter(@Nullable List<User> data) {
        super(R.layout.item_manager_user, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        helper.setText(R.id.tv_name, item.getUserName());

        if ("0".equals(item.getAdminLevel())) {
            ((AppCompatRadioButton) helper.getView(R.id.radioBtn)).setChecked(true);
        } else {
            ((AppCompatRadioButton) helper.getView(R.id.radioBtn)).setChecked(false);
        }
        helper.addOnClickListener(R.id.radioBtn);
    }
}
