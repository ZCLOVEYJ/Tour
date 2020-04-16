package com.max.tour.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * Copyright (C) 2019, Relx
 * RateBean
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-13, ZhengChen, Create file
 */
public class RateBean extends LitePalSupport {

    @Column(unique = true)
    private int id;
    @Column(unique = true)
    private int userId;
    @Column(unique = true)
    private int resortId;
    private int score;
    private int score1;
    private int score2;
    private int score3;
    private int score4;
    private int score5;
    private Date ratingtime;
    private String others;
}
