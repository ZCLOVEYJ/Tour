package com.max.tour.ui.adapter;

import android.support.annotation.Nullable;

import com.amap.api.services.route.BusPath;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.max.tour.R;
import com.max.tour.utils.map.AMapUtil;

import java.util.List;

/**
 * Copyright (C) 2019, Relx
 * BusRouteAdapter
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-21, ZhengChen, Create file
 */
public class BusRouteAdapter extends BaseQuickAdapter<BusPath, BaseViewHolder> {


    public BusRouteAdapter(@Nullable List<BusPath> data) {
        super(R.layout.item_bus_route, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BusPath item) {
        helper.setText(R.id.tv_title, AMapUtil.getBusPathTitle(item));
        helper.setText(R.id.tv_content, AMapUtil.getBusPathDes(item));
    }
}
