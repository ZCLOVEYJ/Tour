package com.max.tour.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.max.tour.R;
import com.max.tour.bean.Comment;
import com.max.tour.bean.User;
import com.max.tour.bean.greendao.CommentDao;

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
public class ManagerCommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {

    public ManagerCommentAdapter(@Nullable List<Comment> data) {
        super(R.layout.item_manager_comment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {
        helper.setText(R.id.tv_name, item.getUserId()+"");
        helper.setText(R.id.tv_comment,item.getContent());
        helper.setText(R.id.tv_time, TimeUtils.date2String(item.getAddtime()));

        helper.addOnClickListener(R.id.tv_delete);


    }
}
