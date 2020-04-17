package com.max.tour.helper;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.blankj.utilcode.util.ToastUtils;
import com.max.tour.bean.AdministratorBean;
import com.max.tour.bean.RateBean;
import com.max.tour.bean.RemarkBean;
import com.max.tour.bean.SightsBean;
import com.max.tour.bean.UserBean;
import com.max.tour.utils.StringUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C) 2019, Relx
 * DBHelper
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-14, ZhengChen, Create file
 */
public class DBHelper {


    public static final String ADMIN = "admin";
    public static final String PASSWORD = "666666";
    public static final String ADMIN_LEVEL = "5";

    /**
     * 查找用户是否存在
     *
     * @param email
     * @return
     */
    public static boolean findUserByEmail(String email) {

        List<UserBean> users = LitePal.findAll(UserBean.class);
        for (UserBean user : users) {
            if (email.trim().equals(user.getEmail())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 登录
     *
     * @param email
     * @param password
     * @return
     */
    public static UserBean findUserByEmailPassword(String email, String password) {

        String pwd = StringUtils.encode(password);
        UserBean bean;

        List<UserBean> users = LitePal.findAll(UserBean.class);
        for (UserBean user : users) {
            if (email.trim().equals(user.getEmail()) && pwd.equals(user.getUserPassword())) {
                bean = user;
                return bean;
            }
        }
        return null;
    }

    /**
     * 初始化一个ADmin
     * 账号 admin
     * 密码 666666
     */
    public static void initAdmin() {
        List<AdministratorBean> admins = LitePal.findAll(AdministratorBean.class);
        for (AdministratorBean admin : admins) {
            if (ADMIN.equals(admin.getAdminname())) {
                return;
            }
        }
        AdministratorBean bean = new AdministratorBean();
        bean.setAdminname(ADMIN);
        bean.setAdminjiassword(StringUtils.encode(PASSWORD));
        bean.setAdminLevel(ADMIN_LEVEL);
        bean.setAddTime(new Date());
        bean.save();
    }

    /**
     * 管理员登录
     *
     * @param name
     * @param password
     * @return
     */
    public static AdministratorBean findAdminByNamePassword(String name, String password) {
        String pwd = StringUtils.encode(password);
        AdministratorBean bean;

        List<AdministratorBean> admins = LitePal.findAll(AdministratorBean.class);
        for (AdministratorBean admin : admins) {
            if (name.trim().equals(admin.getAdminname()) && pwd.equals(admin.getAdminjiassword())) {
                bean = admin;
                return bean;
            }
        }
        return null;

    }

    /**
     * 重置密码
     *
     * @param email
     * @param password
     */
    public static boolean updateUser(String email, String password) {
        String pwd = StringUtils.encode(password);
        UserBean userBean = null;
        List<UserBean> users = LitePal.findAll(UserBean.class);
        for (UserBean user : users) {
            if (email.trim().equals(user.getEmail())) {
                userBean = user;

            }
        }
        if (userBean != null) {
            userBean.setUserPassword(pwd);
            int count = userBean.update(userBean.getId());
            if (count > 0) {
                return true;
            }
        }
        return false;
    }

    public static void findTourByLocation(PoiItem item) {

        boolean hasData = false;
        LatLonPoint latLonPoint = item.getLatLonPoint();

        List<SightsBean> sights = LitePal.findAll(SightsBean.class);
        for (SightsBean sight : sights) {
            if (latLonPoint.getLatitude() == sight.getLatitude() && latLonPoint.getLongitude() == sight.getLongitude()) {
                //判断经纬度
                hasData = true;
            }
        }
        if (!hasData) {
            //  添加数据到数据库
            SightsBean bean = new SightsBean();
            bean.setResortId(item.getPoiId());
            bean.setLatitude(latLonPoint.getLatitude());
            bean.setLongitude(latLonPoint.getLongitude());
            bean.setResortName(item.getTitle());
            bean.setResortGrade("3");
            bean.setResortTime("早8:00--晚6:00");
            bean.setResortPrice(120);
            bean.setResortAddress(item.getSnippet());

            List<RemarkBean> list = new ArrayList<>();
            for (int i=0;i<3;i++){
                RemarkBean a = new RemarkBean();
                a.setContent("你好");
                a.setReply("我不好");
                list.add(a);
            }
            bean.setRemarkList(list);

            List<String> pic = new ArrayList<>();
            for (int i = 0; i < item.getPhotos().size(); i++) {
                pic.add(item.getPhotos().get(i).getUrl());
            }
            bean.setPictures(pic);
            bean.save();

        }


    }

    public static List<SightsBean> findRecommend() {

        return LitePal.findAll(SightsBean.class);
    }

    public static SightsBean findSightByLatLon(PoiItem item) {

        SightsBean bean = null;
        LatLonPoint latLonPoint = item.getLatLonPoint();

        List<SightsBean> sights = LitePal.findAll(SightsBean.class);
        for (SightsBean sight : sights) {
            if (latLonPoint.getLatitude() == sight.getLatitude() && latLonPoint.getLongitude() == sight.getLongitude()) {
                //判断经纬度
                bean = sight;

            }
        }
        return bean;
    }

    /**
     * 根据景区id查询评分
     *
     * @param id
     * @return
     */
    public static float queryRating(int id) {
        float rateCount = 0f;
        List<RateBean> rates = LitePal.findAll(RateBean.class, id);
        for (RateBean rate : rates) {
            rateCount += rate.getScore();
        }
        if (rates.size() == 0) {
            return 0f;
        }
        return rateCount / rates.size();
    }

}
