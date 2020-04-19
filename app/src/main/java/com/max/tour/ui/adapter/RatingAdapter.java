package com.max.tour.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.max.tour.R;
import com.max.tour.bean.Rate;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.List;


public class RatingAdapter extends BaseQuickAdapter<Rate, BaseViewHolder> {

    private Context mContext;

    public RatingAdapter(Context context, @Nullable List<Rate> data) {
        super(R.layout.item_rate, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Rate item) {

        helper.setText(R.id.tv_name, item.getUserId() + "");
        helper.setText(R.id.tv_time, TimeUtils.date2String(item.getRatingtime()));
        helper.setText(R.id.tv_score, item.getScore() + "分");
        ScaleRatingBar ratingBar = ((ScaleRatingBar) helper.getView(R.id.ratingBar));
        ratingBar.setClickable(false);
        ratingBar.setScrollable(false);
        ratingBar.setClearRatingEnabled(false);
        ratingBar.setRating(item.getScore());

    }
}
