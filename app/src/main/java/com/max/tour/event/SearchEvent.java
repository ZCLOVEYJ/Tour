package com.max.tour.event;

import com.amap.api.services.help.Tip;

/**
 * Copyright (C) 2019, Relx
 * SearchEvent
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-18, ZhengChen, Create file
 */
public class SearchEvent {

    private int tag;

    private String keywords;

    private Tip tip;

    private int type;

    private String locationName;

    private double lau;

    private double lon;


    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Tip getTip() {
        return tip;
    }

    public void setTip(Tip tip) {
        this.tip = tip;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLau() {
        return lau;
    }

    public void setLau(double lau) {
        this.lau = lau;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
