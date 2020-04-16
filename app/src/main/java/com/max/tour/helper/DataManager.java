package com.max.tour.helper;

import com.max.tour.http.model.HttpData;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Copyright (C) 2019, Relx
 * DataManager
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-14, ZhengChen, Create file
 */
public class DataManager {


    public static void register(Observer<HttpData<String>> observer) {

        Observable.create(new ObservableOnSubscribe<HttpData<String>>() {
            @Override
            public void subscribe(ObservableEmitter<HttpData<String>> emitter) throws Exception {

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());


    }


}
