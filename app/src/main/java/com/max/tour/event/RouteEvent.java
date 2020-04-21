package com.max.tour.event;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;

import java.util.List;

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


    private DriveRouteResult driveRouteResult;

    private WalkRouteResult walkRouteResult;

    private BusRouteResult busRouteResult;

    private LatLonPoint startPoint ;
    private LatLonPoint endPoint;

    private String startStr;

    private String endStr;



    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
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


    public LatLonPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(LatLonPoint startPoint) {
        this.startPoint = startPoint;
    }

    public LatLonPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(LatLonPoint endPoint) {
        this.endPoint = endPoint;
    }

    public String getStartStr() {
        return startStr;
    }

    public void setStartStr(String startStr) {
        this.startStr = startStr;
    }

    public String getEndStr() {
        return endStr;
    }

    public void setEndStr(String endStr) {
        this.endStr = endStr;
    }
}
