package com.max.tour.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Copyright (C) 2019, Relx
 * Route
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-13, ZhengChen, Create file
 */
@Entity
public class Route implements Serializable {

    public static final long  serialVersionUID = 5L;

    @Id(autoincrement = true)
    private Long id;

    private Integer routeId;
    private String routeName;
    private String routeLength;
    private String Start;
    private Integer routeDay;
    private Integer routePrice;
    private String routeDetails;
    private Date routeAddtime;
    private String others;
    @Generated(hash = 558307425)
    public Route(Long id, Integer routeId, String routeName, String routeLength,
            String Start, Integer routeDay, Integer routePrice, String routeDetails,
            Date routeAddtime, String others) {
        this.id = id;
        this.routeId = routeId;
        this.routeName = routeName;
        this.routeLength = routeLength;
        this.Start = Start;
        this.routeDay = routeDay;
        this.routePrice = routePrice;
        this.routeDetails = routeDetails;
        this.routeAddtime = routeAddtime;
        this.others = others;
    }
    @Generated(hash = 467763370)
    public Route() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getRouteId() {
        return this.routeId;
    }
    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }
    public String getRouteName() {
        return this.routeName;
    }
    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
    public String getRouteLength() {
        return this.routeLength;
    }
    public void setRouteLength(String routeLength) {
        this.routeLength = routeLength;
    }
    public String getStart() {
        return this.Start;
    }
    public void setStart(String Start) {
        this.Start = Start;
    }
    public Integer getRouteDay() {
        return this.routeDay;
    }
    public void setRouteDay(Integer routeDay) {
        this.routeDay = routeDay;
    }
    public Integer getRoutePrice() {
        return this.routePrice;
    }
    public void setRoutePrice(Integer routePrice) {
        this.routePrice = routePrice;
    }
    public String getRouteDetails() {
        return this.routeDetails;
    }
    public void setRouteDetails(String routeDetails) {
        this.routeDetails = routeDetails;
    }
    public Date getRouteAddtime() {
        return this.routeAddtime;
    }
    public void setRouteAddtime(Date routeAddtime) {
        this.routeAddtime = routeAddtime;
    }
    public String getOthers() {
        return this.others;
    }
    public void setOthers(String others) {
        this.others = others;
    }


}
