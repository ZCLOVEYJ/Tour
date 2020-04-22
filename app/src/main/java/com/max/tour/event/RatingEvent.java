package com.max.tour.event;

/**
 * Copyright (C) 2019, Relx
 * RatingEvent
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-22, ZhengChen, Create file
 */
public class RatingEvent {

    private boolean refresh;

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }
}
