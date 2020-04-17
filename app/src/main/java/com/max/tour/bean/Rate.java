package com.max.tour.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Copyright (C) 2019, Relx
 * Rate
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-13, ZhengChen, Create file
 */
@Entity
public class Rate implements Serializable {

    public static final long  serialVersionUID = 4L;

    @Id(autoincrement = true)
    private Long id;
    @Property
    private Long userId;
    @Property
    private Long sightId;
    @Property
    private Integer score;
    @Property
    private Date ratingtime;
    @Property
    private String others;
    @Generated(hash = 2049468307)
    public Rate(Long id, Long userId, Long sightId, Integer score, Date ratingtime,
            String others) {
        this.id = id;
        this.userId = userId;
        this.sightId = sightId;
        this.score = score;
        this.ratingtime = ratingtime;
        this.others = others;
    }
    @Generated(hash = 1992118559)
    public Rate() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getSightId() {
        return this.sightId;
    }
    public void setSightId(Long sightId) {
        this.sightId = sightId;
    }
    public Integer getScore() {
        return this.score;
    }
    public void setScore(Integer score) {
        this.score = score;
    }
    public Date getRatingtime() {
        return this.ratingtime;
    }
    public void setRatingtime(Date ratingtime) {
        this.ratingtime = ratingtime;
    }
    public String getOthers() {
        return this.others;
    }
    public void setOthers(String others) {
        this.others = others;
    }


}
