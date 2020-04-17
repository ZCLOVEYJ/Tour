package com.max.tour.helper;

import com.max.tour.http.model.HttpData;

/**
 * Copyright (C) 2019, Relx
 * DataUtils
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-16, ZhengChen, Create file
 */
public class DataUtils<T> {


    private static DataUtils sInstance;


    public static DataUtils getInstance() {

        if (sInstance == null) {
            sInstance = new DataUtils();
        }

        return sInstance;
    }

    private DataUtils() {

    }

    public HttpData<T> getData(int code, String msg, T bean) {
        HttpData<T> data = new HttpData<>();
        data.setCode(code);
        data.setMsg(msg);
        data.setData(bean);
        return data;
    }
}
