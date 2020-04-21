package com.max.tour.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

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

    private String startLocation;
    private String endLocation;
    private Double startLongitude;
    private Double startLatitude;

    private Double endLongitude;
    private Double endLatitude;
    @Generated(hash = 726973792)
    public Route(Long id, String startLocation, String endLocation,
            Double startLongitude, Double startLatitude, Double endLongitude,
            Double endLatitude) {
        this.id = id;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startLongitude = startLongitude;
        this.startLatitude = startLatitude;
        this.endLongitude = endLongitude;
        this.endLatitude = endLatitude;
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
    public String getStartLocation() {
        return this.startLocation;
    }
    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }
    public String getEndLocation() {
        return this.endLocation;
    }
    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }
    public Double getStartLongitude() {
        return this.startLongitude;
    }
    public void setStartLongitude(Double startLongitude) {
        this.startLongitude = startLongitude;
    }
    public Double getStartLatitude() {
        return this.startLatitude;
    }
    public void setStartLatitude(Double startLatitude) {
        this.startLatitude = startLatitude;
    }
    public Double getEndLongitude() {
        return this.endLongitude;
    }
    public void setEndLongitude(Double endLongitude) {
        this.endLongitude = endLongitude;
    }
    public Double getEndLatitude() {
        return this.endLatitude;
    }
    public void setEndLatitude(Double endLatitude) {
        this.endLatitude = endLatitude;
    }




}
