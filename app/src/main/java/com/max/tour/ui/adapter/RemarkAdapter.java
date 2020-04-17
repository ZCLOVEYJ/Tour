package com.max.tour.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.max.tour.R;
import com.max.tour.bean.Comment;

import java.util.List;

/**
 * 评论Adapter
 */
public class RemarkAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {

private Context mContext;


    public RemarkAdapter(Context context, @Nullable List<Comment> data) {
        super(R.layout.item_remark, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {

    }
}
