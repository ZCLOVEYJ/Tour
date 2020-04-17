package com.max.tour.helper;

import com.amap.api.services.core.PoiItem;
import com.blankj.utilcode.util.LogUtils;
import com.max.tour.app.MyApp;
import com.max.tour.bean.Admin;
import com.max.tour.bean.Sights;
import com.max.tour.bean.User;
import com.max.tour.bean.greendao.AdminDao;
import com.max.tour.bean.greendao.DaoSession;
import com.max.tour.bean.greendao.UserDao;
import com.max.tour.utils.StringUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.List;

/**
 * 数据库帮助类
 */
public class DbHelper {


    private static final String ADMIN = "admin";
    private static final String PASSWORD = "666666";
    private static final String ADMIN_LEVEL = "5";


    /**
     * 查找用户是否存在
     *
     * @param email 邮箱账号
     * @return 是否存在
     */
    public static boolean findUserByEmail(String email) {

        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<User> qb = daoSession.queryBuilder(User.class);
        qb.where(UserDao.Properties.Email.eq(email));
        List<User> users = qb.list();
        return users.size() > 0;
    }

    /**
     * 登录
     *
     * @param email    邮箱
     * @param password 密码
     * @return 用户信息
     */
    public static User findUserByEmailPassword(String email, String password) {

        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<User> qb = daoSession.queryBuilder(User.class);
        qb.where(UserDao.Properties.Email.eq(email), UserDao.Properties.UserPassword.eq(password));
        List<User> users = qb.list();
        if (users.size() == 1) {
            return users.get(0);
        }
        return null;
    }

    /**
     * 初始化一个ADmin
     * 账号 admin
     * 密码 666666
     */
    public static void initAdmin() {

        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<Admin> qb = daoSession.queryBuilder(Admin.class);
        qb.where(AdminDao.Properties.Adminname.eq(ADMIN));
        List<Admin> users = qb.list();
        if (users.size() > 0) {
            LogUtils.iTag("TAG", "管理员已经存在,无需创建");
            return;
        }
        // 不存在,就创建
        Admin bean = new Admin();
        bean.setAdminname(ADMIN);
        bean.setAdminjiassword(StringUtils.encode(PASSWORD));
        bean.setAdminLevel(ADMIN_LEVEL);
        bean.setAddTime(new Date());
        daoSession.insert(bean);

    }

    /**
     * 管理员登录
     *
     * @param name     账号
     * @param password 密码
     * @return 管理员信息
     */
    public static Admin findAdminByNamePassword(String name, String password) {
        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<Admin> qb = daoSession.queryBuilder(Admin.class);
        qb.where(AdminDao.Properties.Adminname.eq(name), AdminDao.Properties.Adminjiassword.eq(password));
        List<Admin> users = qb.list();
        if (users.size() == 1) {
            return users.get(0);
        }
        return null;
    }

    /**
     * 注册用户
     *
     * @param email    邮箱
     * @param password 密码
     */
    public static User insertUser(String email, String password) {
        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        User user = new User();
        user.setUserName("user_" + System.currentTimeMillis());
        user.setEmail(email);
        user.setUserPassword(password);
        user.setName("");
        user.setSex("女");
        user.setBirthday("");
        user.setUserIcon("");
        user.setInfo("");
        user.setAdminLevel("0");
        if (daoSession.insert(user) > 0) {
            return user;
        }
        return null;

    }

    /**
     * 重置密码
     *
     * @param email    邮箱
     * @param password 密码
     */
    public static boolean updateUser(String email, String password) {
        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<User> qb = daoSession.queryBuilder(User.class);
        qb.where(UserDao.Properties.Email.eq(email));
        List<User> users = qb.list();
        if (users.size() > 0) {
            User user = users.get(0);
            user.setEmail(email);
            user.setUserPassword(password);
            daoSession.update(user);
            return true;
        }
        return false;

    }

    public static void findTourByLocation(PoiItem item) {

//        boolean hasData = false;
//        LatLonPoint latLonPoint = item.getLatLonPoint();
//
//        List<Sights> sights = LitePal.findAll(Sights.class);
//        for (Sights sight : sights) {
//            if (latLonPoint.getLatitude() == sight.getLatitude() && latLonPoint.getLongitude() == sight.getLongitude()) {
//                //判断经纬度
//                hasData = true;
//            }
//        }
//        if (!hasData) {
//            //  添加数据到数据库
//            Sights bean = new Sights();
//            bean.setResortId(item.getPoiId());
//            bean.setLatitude(latLonPoint.getLatitude());
//            bean.setLongitude(latLonPoint.getLongitude());
//            bean.setResortName(item.getTitle());
//            bean.setResortGrade("3");
//            bean.setResortTime("早8:00--晚6:00");
//            bean.setResortPrice(120);
//            bean.setResortAddress(item.getSnippet());
//
//
//            List<String> pic = new ArrayList<>();
//            for (int i = 0; i < item.getPhotos().size(); i++) {
//                pic.add(item.getPhotos().get(i).getUrl());
//            }
//            bean.setPictures(pic);
//            bean.save();

//        }


    }

    public static List<Sights> findRecommend() {

        return null;
    }

    public static Sights findSightByLatLon(PoiItem item) {

//        Sights bean = null;
//        LatLonPoint latLonPoint = item.getLatLonPoint();
//
//        List<Sights> sights = LitePal.findAll(Sights.class);
//        for (Sights sight : sights) {
//            if (latLonPoint.getLatitude() == sight.getLatitude() && latLonPoint.getLongitude() == sight.getLongitude()) {
//                //判断经纬度
//                bean = sight;
//
//            }
//        }
        return null;
    }

    /**
     * 根据景区id查询评分
     *
     * @param id
     * @return
     */
    public static float queryRating(int id) {
//        float rateCount = 0f;
//        List<Rate> rates = LitePal.findAll(Rate.class, id);
//        for (Rate rate : rates) {
//            rateCount += rate.getScore();
//        }
//        if (rates.size() == 0) {
//            return 0f;
//        }
//        return rateCount / rates.size();
//    }
//
//    public static void saveComment(String msg, int mUserId, int mSightId) {
//
//        Comment comment = new Comment();
//        comment.setContent(msg);
//        comment.setAddtime(new Date());
//        comment.setReply("");
//        comment.save();
//
//        User user = LitePal.find(User.class, mUserId);
//        user.getComments().add(comment);
//        user.save();
//
//        Sights sights = LitePal.find(Sights.class, mSightId);
//        sights.getComments().add(comment);
//        sights.save();
        return 0f;


    }


}
