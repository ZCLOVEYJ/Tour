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
}
