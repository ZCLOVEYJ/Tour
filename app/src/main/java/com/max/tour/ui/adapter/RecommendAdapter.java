package com.max.tour.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.max.tour.R;
import com.max.tour.bean.Sights;

import java.util.List;

/**
 * Copyright (C) 2019, Relx
 * RecommendAdapter
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-16, ZhengChen, Create file
 */
public class RecommendAdapter extends BaseQuickAdapter<Sights, BaseViewHolder> {


    private Context mContext;


    public RecommendAdapter(Context context, @Nullable List<Sights> data) {
        super(R.layout.item_recommend, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Sights item) {
        helper.setText(R.id.tv_title, item.getResortName());
        Glide.with(mContext)
                .load((item.getPictures() != null && item.getPictures().size() > 0) ? item.getPictures().get(0) : "")
                .placeholder(R.mipmap.bg_default_photo)
                .into((ImageView) helper.getView(R.id.iv_pic));
    }
}
