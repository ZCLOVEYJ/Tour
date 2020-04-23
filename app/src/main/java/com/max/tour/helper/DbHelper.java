package com.max.tour.helper;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.blankj.utilcode.util.LogUtils;
import com.max.tour.app.MyApp;
import com.max.tour.bean.Admin;
import com.max.tour.bean.Comment;
import com.max.tour.bean.Picture;
import com.max.tour.bean.Rate;
import com.max.tour.bean.Route;
import com.max.tour.bean.Sights;
import com.max.tour.bean.User;
import com.max.tour.bean.greendao.AdminDao;
import com.max.tour.bean.greendao.CommentDao;
import com.max.tour.bean.greendao.DaoSession;
import com.max.tour.bean.greendao.RateDao;
import com.max.tour.bean.greendao.RouteDao;
import com.max.tour.bean.greendao.SightsDao;
import com.max.tour.bean.greendao.UserDao;
import com.max.tour.constants.Constant;
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

    /**
     * 修改用户名
     *
     * @param name
     */
    public static boolean updateUser(long userId, String name) {
        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<User> qb = daoSession.queryBuilder(User.class);
        qb.where(UserDao.Properties.Id.eq(userId));
        List<User> users = qb.list();
        if (users.size() > 0) {
            User user = users.get(0);
            user.setUserName(name);
            daoSession.update(user);
            return true;
        }
        return false;

    }

    /**
     * 修改权限
     */
    public static boolean updateUserWithPosition(long userId, String level) {
        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<User> qb = daoSession.queryBuilder(User.class);
        qb.where(UserDao.Properties.Id.eq(userId));
        List<User> users = qb.list();
        if (users.size() > 0) {
            User user = users.get(0);
            user.setAdminLevel(level);
            daoSession.update(user);
            return true;
        }
        return false;

    }

    public static void saveTourByLocation(PoiItem item) {

        boolean hasData = false;
        LatLonPoint latLonPoint = item.getLatLonPoint();
        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<Sights> qb = daoSession.queryBuilder(Sights.class);
        qb.where(SightsDao.Properties.Latitude.eq(latLonPoint.getLatitude()), SightsDao.Properties.Longitude.eq(latLonPoint.getLongitude()));
        List<Sights> sights = qb.list();
        if (sights.size() > 0) {
            hasData = true;
        }
        if (!hasData) {
            //  添加数据到数据库
            Sights sight = new Sights();
            sight.setResortId(item.getPoiId());
            sight.setLatitude(latLonPoint.getLatitude());
            sight.setLongitude(latLonPoint.getLongitude());
            sight.setResortName(item.getTitle());
            sight.setResortGrade("3");
            sight.setResortTime("早8:00--晚6:00");
            sight.setResortPrice(120);
            sight.setResortAddress(item.getSnippet());
            daoSession.insert(sight);

            for (int i = 0; i < item.getPhotos().size(); i++) {
                Picture picture = new Picture();
                picture.setPath(item.getPhotos().get(i).getUrl());
                picture.setSightId(sight.getId());
                daoSession.insert(picture);
            }

        }


    }

    /**
     * 查询推荐的景点
     *
     * @return 景点列表
     */
    public static List<Sights> findRecommend() {

        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<Sights> qb = daoSession.queryBuilder(Sights.class);
        qb.orderDesc(SightsDao.Properties.ResortScore);

        return qb.list();
    }

    /**
     * 查询单个景点
     *
     * @param item 位置
     * @return 景点信息
     */
    public static Sights findSightByLatLon(PoiItem item) {

        LatLonPoint latLonPoint = item.getLatLonPoint();
        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<Sights> qb = daoSession.queryBuilder(Sights.class);
        qb.where(SightsDao.Properties.Latitude.eq(latLonPoint.getLatitude()), SightsDao.Properties.Longitude.eq(latLonPoint.getLongitude()));
        List<Sights> sights = qb.list();
        if (sights.size() > 0) {
            return sights.get(0);
        }
        return null;
    }

    public static Sights findSightByLatLon(LatLng latLng) {

        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<Sights> qb = daoSession.queryBuilder(Sights.class);
        qb.where(SightsDao.Properties.Latitude.eq(latLng.latitude), SightsDao.Properties.Longitude.eq(latLng.longitude));
        List<Sights> sights = qb.list();
        if (sights.size() > 0) {
            return sights.get(0);
        }
        return null;
    }

    public static Sights findSightByLatLon(long sightId) {

        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<Sights> qb = daoSession.queryBuilder(Sights.class);
        qb.where(SightsDao.Properties.Id.eq(sightId));
        List<Sights> sights = qb.list();
        if (sights.size() > 0) {
            return sights.get(0);
        }
        return null;
    }


    /**
     * 提交评论
     *
     * @param msg      消息
     * @param mUserId  用户ID
     * @param mSightId 景区ID
     * @return ture = 提交成功
     */
    public static boolean saveComment(String msg, long mUserId, long mSightId) {


        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        Comment comment = new Comment();
        comment.setUserId(mUserId);
        comment.setSightId(mSightId);
        comment.setUserIcon(Constant.mUserIcon);
        comment.setContent(msg);
        comment.setAddtime(new Date());
        comment.setReply("");
        return daoSession.insert(comment) > 0;
    }


    /**
     * 获取评论列表
     *
     * @return 评论列表
     */
    public static List<Comment> getComments(long mSightId) {
        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        // 提交成功  查询评论列表
        QueryBuilder<Comment> qb = daoSession.queryBuilder(Comment.class);
        qb.where(CommentDao.Properties.SightId.eq(mSightId));
        return qb.list();
    }

    public static List<Rate> getSightRating(long mSightId) {
        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        // 提交成功  查询评论列表
        QueryBuilder<Rate> qb = daoSession.queryBuilder(Rate.class);
        qb.where(RateDao.Properties.SightId.eq(mSightId));
        return qb.list();
    }

    public static float getAverageRating(long mSightId) {
        float rating = 0f;
        List<Rate> list = getSightRating(mSightId);
        if (list.size() == 0) {
            return rating;
        }
        for (Rate rate : list) {
            rating += rate.getScore();
        }
        return rating / list.size();
    }

    /**
     * 恢复评论
     *
     * @param commendId
     * @param msg
     */
    public static boolean updateComment(long commendId, String msg) {

        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<Comment> qb = daoSession.queryBuilder(Comment.class);
        qb.where(CommentDao.Properties.Id.eq(commendId));
        List<Comment> comments = qb.list();
        if (comments.size() > 0) {
            Comment comment = comments.get(0);
            comment.setReply(msg);
            daoSession.update(comment);
            return true;
        }
        return false;
    }

    public static Rate querySightWithUserRating(long userId, long sightId) {
        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<Rate> qb = daoSession.queryBuilder(Rate.class);
        qb.where(RateDao.Properties.UserId.eq(userId)).where(RateDao.Properties.SightId.eq(sightId));
        List<Rate> list = qb.list();
        if (list.size() > 0) {
            Rate rate = list.get(0);
            return rate;
        }
        return null;
    }


    public static List<Rate> querySightRating(long sightId) {
        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<Rate> qb = daoSession.queryBuilder(Rate.class);
        qb.where(RateDao.Properties.SightId.eq(sightId));
        return qb.list();
    }

    public static boolean commitRating(long sightId, long userId, float ratings) {

        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        Rate rate = new Rate();
        rate.setUserId(userId);
        rate.setSightId(sightId);
        rate.setScore(ratings);
        rate.setRatingtime(new Date());
        if (daoSession.insert(rate) > 0) {
            double score = getAverageRating(sightId);
            Sights sights = findSightByLatLon(sightId);
            sights.setResortScore(score);
            daoSession.update(sights);
            return true;
        } else {
            return false;
        }
    }

    public static boolean insertRoute(String toString, String toString1, double latitude, double longitude, double latitude1, double longitude1) {


        DaoSession daoSession = MyApp.getApplication().getDaoSession();

        Route route = queryRoute(toString, toString1, latitude, longitude, latitude1, longitude1);
        if (route != null) {
            return true;
        }
        route = new Route();
        route.setStartLocation(toString);
        route.setEndLocation(toString1);
        route.setStartLatitude(latitude);
        route.setStartLongitude(longitude);
        route.setEndLatitude(latitude1);
        route.setEndLongitude(longitude1);

        return daoSession.insert(route) > 0;

    }

    public static Route queryRoute(String toString, String toString1, double latitude, double longitude, double latitude1, double longitude1) {
        DaoSession daoSession = MyApp.getApplication().getDaoSession();

        QueryBuilder<Route> qb = daoSession.queryBuilder(Route.class);
        qb.where(RouteDao.Properties.StartLatitude.eq(latitude)).where(RouteDao.Properties.StartLongitude.eq(longitude))
                .where(RouteDao.Properties.EndLatitude.eq(latitude1)).where(RouteDao.Properties.EndLongitude.eq(longitude1));
        List<Route> list = qb.list();
        if (list.size() > 0) {
            Route route = list.get(0);
            return route;
        }
        return null;
    }

    public static List<Route> queryRoute() {
        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<Route> qb = daoSession.queryBuilder(Route.class);
        return qb.list();
    }

    public static List<User> queryUsers() {
        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<User> qb = daoSession.queryBuilder(User.class);
        return qb.list();
    }

    public static List<Comment> queryComment() {
        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<Comment> qb = daoSession.queryBuilder(Comment.class);
        return qb.list();
    }

    public static boolean deleteCommentWithId(long commentId) {
        DaoSession daoSession = MyApp.getApplication().getDaoSession();
        QueryBuilder<Comment> qb = daoSession.queryBuilder(Comment.class);
        qb.where(CommentDao.Properties.Id.eq(commentId));
        List<Comment> list = qb.list();
        if (list != null && list.size() > 0) {
            daoSession.delete(list.get(0));
            return true;
        }
        return false;
    }
}
