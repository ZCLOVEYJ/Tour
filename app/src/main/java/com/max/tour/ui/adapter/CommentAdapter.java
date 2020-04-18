package com.max.tour.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.max.tour.R;
import com.max.tour.bean.Comment;

import java.util.List;

/**
 * 评论Adapter
 */
public class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {

    private Context mContext;


    public CommentAdapter(Context context, @Nullable List<Comment> data) {
        super(R.layout.item_comment, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {

        helper.setText(R.id.tv_name, item.getUserId() + "");
        helper.setText(R.id.tv_conent, item.getContent());
        helper.setText(R.id.tv_time, TimeUtils.date2String(item.getAddtime()));
        if (TextUtils.isEmpty(item.getReply())) {
            helper.setGone(R.id.llyt_reply, false);
            helper.setGone(R.id.tv_reply, true).addOnClickListener(R.id.tv_reply);
        } else {
            helper.setGone(R.id.llyt_reply, true);
            helper.setText(R.id.tv_first_reply, item.getReply());
            helper.setGone(R.id.tv_reply, false);
        }


    }
}
