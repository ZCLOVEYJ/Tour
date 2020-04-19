package com.max.tour.event;

import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.WalkRouteResult;

/**
 * Copyright (C) 2019, Relx
 * RouteEvent
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-19, ZhengChen, Create file
 */
public class RouteEvent {

    /**
     * route type
     */
    private int tag;

    private DrivePath path;


    private DriveRouteResult driveRouteResult;

    private WalkRouteResult walkRouteResult;

    private BusRouteResult busRouteResult;


    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public DrivePath getPath() {
        return path;
    }

    public void setPath(DrivePath path) {
        this.path = path;
    }

    public DriveRouteResult getDriveRouteResult() {
        return driveRouteResult;
    }

    public void setDriveRouteResult(DriveRouteResult driveRouteResult) {
        this.driveRouteResult = driveRouteResult;
    }

    public WalkRouteResult getWalkRouteResult() {
        return walkRouteResult;
    }

    public void setWalkRouteResult(WalkRouteResult walkRouteResult) {
        this.walkRouteResult = walkRouteResult;
    }

    public BusRouteResult getBusRouteResult() {
        return busRouteResult;
    }

    public void setBusRouteResult(BusRouteResult busRouteResult) {
        this.busRouteResult = busRouteResult;
    }
}
