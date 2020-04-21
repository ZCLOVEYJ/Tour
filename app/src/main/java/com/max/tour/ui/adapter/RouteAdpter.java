package com.max.tour.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.max.tour.R;
import com.max.tour.bean.Route;

import java.util.List;


public class RouteAdpter extends BaseQuickAdapter<Route, BaseViewHolder> {
    public RouteAdpter(@Nullable List<Route> data) {
        super(R.layout.item_route, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Route item) {

        helper.setText(R.id.tv_route, item.getStartLocation() + " -> " + item.getEndLocation());

    }
}
