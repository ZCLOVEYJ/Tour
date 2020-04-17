package com.max.tour.bean;

import com.blankj.utilcode.util.CollectionUtils;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2019, Relx
 * SightsBean
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-13, ZhengChen, Create file
 */
public class SightsBean extends LitePalSupport implements Serializable {

    @Column(unique = true)
    private int id;
    @Column(unique = true)
    private String resortId;
    private String resortName;
    private String resortAddress;
    private String resortGrade;
    private int resortPrice;
    private String resortTime;
    private String resortContent;

    /**
     * 经度
     */
    private double longitude;
    /**
     * 纬度
     */
    private List<String> pictures;

    private double latitude;

    private List<RemarkBean> mRemarkList;
    private List<RateBean> mRateList;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResortId() {
        return resortId;
    }

    public void setResortId(String resortId) {
        this.resortId = resortId;
    }

    public String getResortName() {
        return resortName;
    }

    public void setResortName(String resortName) {
        this.resortName = resortName;
    }

    public String getResortAddress() {
        return resortAddress;
    }

    public void setResortAddress(String resortAddress) {
        this.resortAddress = resortAddress;
    }

    public String getResortGrade() {
        return resortGrade;
    }

    public void setResortGrade(String resortGrade) {
        this.resortGrade = resortGrade;
    }

    public int getResortPrice() {
        return resortPrice;
    }

    public void setResortPrice(int resortPrice) {
        this.resortPrice = resortPrice;
    }

    public String getResortTime() {
        return resortTime;
    }

    public void setResortTime(String resortTime) {
        this.resortTime = resortTime;
    }

    public String getResortContent() {
        return resortContent;
    }

    public void setResortContent(String resortContent) {
        this.resortContent = resortContent;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public List<RemarkBean> getRemarkList() {
        //子表中会生成一个关联父表的id供父表查询，且字表中id生成符合规则："父表类名小写_id"
        //若父表为Person类(父表中会自动生成一个id自增列)，子表为User类,则字表中会自动生成字段person_id对应父表中id，以供查询
        String linkId = this.getClass().getSimpleName().toLowerCase();
        mRemarkList = LitePal.where(linkId + "_id=?", String.valueOf(id)).find(RemarkBean.class);
        if (mRemarkList == null) {
            mRemarkList = new ArrayList<>();
        }
        return mRemarkList;

    }

    public void setRemarkList(List<RemarkBean> list) {
        //
        if (!CollectionUtils.isEmpty(list)) {
            LitePal.saveAll(list);
        }
        this.mRemarkList = list;

    }

    public List<RateBean> getRateList() {
        //子表中会生成一个关联父表的id供父表查询，且字表中id生成符合规则："父表类名小写_id"
        //若父表为Person类(父表中会自动生成一个id自增列)，子表为User类,则字表中会自动生成字段person_id对应父表中id，以供查询
        String linkId = this.getClass().getSimpleName().toLowerCase();
        mRateList = LitePal.where(linkId + "_id=?", String.valueOf(id)).find(RateBean.class);
        if (mRateList == null) {
            mRateList = new ArrayList<>();
        }
        return mRateList;

    }

    public void setRateList(List<RateBean> list) {
        //
        if (!CollectionUtils.isEmpty(list)) {
            LitePal.saveAll(list);
        }
        this.mRateList = list;

    }

}
