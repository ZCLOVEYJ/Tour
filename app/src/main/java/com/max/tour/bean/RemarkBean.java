package com.max.tour.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * Copyright (C) 2019, Relx
 * RemarkBean
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-13, ZhengChen, Create file
 */
public class RemarkBean extends LitePalSupport {

    @Column(unique = true)
    private int id;
    @Column()
    private int userId;
    private String userIcon;
    private Date addtime;
    private String content;
    private String reply;


}
