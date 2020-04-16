package com.max.tour.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Copyright (C) 2019, Relx
 * SightsPicBean
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-15, ZhengChen, Create file
 */
public class SightsPicBean extends LitePalSupport {

    @Column(unique = true)
    private int id;

    private String path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
