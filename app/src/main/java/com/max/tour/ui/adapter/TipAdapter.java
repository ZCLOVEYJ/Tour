package com.max.tour.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.amap.api.services.help.Tip;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.max.tour.R;

import java.util.List;

/**
 * Copyright (C) 2019, Relx
 * TipAdapter
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-18, ZhengChen, Create file
 */
public class TipAdapter extends BaseQuickAdapter<Tip, BaseViewHolder> {


    public TipAdapter(@Nullable List<Tip> data) {
        super(R.layout.item_tip, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Tip item) {

        helper.setText(R.id.tv_title,item.getName());
        String address = item.getAddress();
        if (TextUtils.isEmpty(address)){
            helper.setGone(R.id.tv_content,false);
        }else {
            helper.setText(R.id.tv_content,address).setGone(R.id.tv_content,true);
        }


    }
}
