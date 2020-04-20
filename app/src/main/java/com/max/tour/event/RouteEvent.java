package com.max.tour.event;

import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.WalkPath;
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

    private DrivePath drivePath;
    private BusPath busPath;
    private WalkPath walkPath;


    private DriveRouteResult driveRouteResult;

    private WalkRouteResult walkRouteResult;

    private BusRouteResult busRouteResult;


    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public DrivePath getDrivePath() {
        return drivePath;
    }

    public void setDrivePath(DrivePath drivePath) {
        this.drivePath = drivePath;
    }

    public BusPath getBusPath() {
        return busPath;
    }

    public void setBusPath(BusPath busPath) {
        this.busPath = busPath;
    }

    public WalkPath getWalkPath() {
        return walkPath;
    }

    public void setWalkPath(WalkPath walkPath) {
        this.walkPath = walkPath;
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
