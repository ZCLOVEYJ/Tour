package com.max.tour.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * Copyright (C) 2019, Relx
 * RouteBean
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-13, ZhengChen, Create file
 */
public class RouteBean extends LitePalSupport {

    @Column(unique = true)
    private int id;
    @Column(unique = true)
    private int routeId;
    private String routeName;
    private String routeLength;
    private String Start;
    private int routeDay;
    private int routePrice;
    private String routeDetails;
    private Date routeAddtime;
    private String others;


}
