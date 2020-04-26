package com.max.tour.bean;

import com.max.tour.bean.greendao.CommentDao;
import com.max.tour.bean.greendao.DaoSession;
import com.max.tour.bean.greendao.PictureDao;
import com.max.tour.bean.greendao.RateDao;
import com.max.tour.bean.greendao.SightsDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) 2019, Relx
 * Sights
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-13, ZhengChen, Create file
 */
@Entity
public class Sights implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long id;
    private Long userId;
    private String resortId;
    private String resortName;
    private String resortAddress;
    private String resortGrade;
    private Integer resortPrice;
    private String resortTime;
    private String resortContent;

    private Double resortScore;

    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;

    private Integer count;



    @ToMany(referencedJoinProperty = "sightId")
    private List<Picture> pictures;


    @ToMany(referencedJoinProperty = "sightId")
    private List<Comment> comments ;

    @ToMany(referencedJoinProperty = "sightId")
    private List<Rate> rates ;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1833270973)
    private transient SightsDao myDao;

    @Generated(hash = 408604138)
    public Sights(Long id, Long userId, String resortId, String resortName, String resortAddress,
            String resortGrade, Integer resortPrice, String resortTime, String resortContent,
            Double resortScore, Double longitude, Double latitude, Integer count) {
        this.id = id;
        this.userId = userId;
        this.resortId = resortId;
        this.resortName = resortName;
        this.resortAddress = resortAddress;
        this.resortGrade = resortGrade;
        this.resortPrice = resortPrice;
        this.resortTime = resortTime;
        this.resortContent = resortContent;
        this.resortScore = resortScore;
        this.longitude = longitude;
        this.latitude = latitude;
        this.count = count;
    }

    @Generated(hash = 252880681)
    public Sights() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getResortId() {
        return this.resortId;
    }

    public void setResortId(String resortId) {
        this.resortId = resortId;
    }

    public String getResortName() {
        return this.resortName;
    }

    public void setResortName(String resortName) {
        this.resortName = resortName;
    }

    public String getResortAddress() {
        return this.resortAddress;
    }

    public void setResortAddress(String resortAddress) {
        this.resortAddress = resortAddress;
    }

    public String getResortGrade() {
        return this.resortGrade;
    }

    public void setResortGrade(String resortGrade) {
        this.resortGrade = resortGrade;
    }

    public Integer getResortPrice() {
        return this.resortPrice;
    }

    public void setResortPrice(Integer resortPrice) {
        this.resortPrice = resortPrice;
    }

    public String getResortTime() {
        return this.resortTime;
    }

    public void setResortTime(String resortTime) {
        this.resortTime = resortTime;
    }

    public String getResortContent() {
        return this.resortContent;
    }

    public void setResortContent(String resortContent) {
        this.resortContent = resortContent;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1058425640)
    public List<Picture> getPictures() {
        if (pictures == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PictureDao targetDao = daoSession.getPictureDao();
            List<Picture> picturesNew = targetDao._querySights_Pictures(id);
            synchronized (this) {
                if (pictures == null) {
                    pictures = picturesNew;
                }
            }
        }
        return pictures;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1035739203)
    public synchronized void resetPictures() {
        pictures = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 623945183)
    public List<Comment> getComments() {
        if (comments == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CommentDao targetDao = daoSession.getCommentDao();
            List<Comment> commentsNew = targetDao._querySights_Comments(id);
            synchronized (this) {
                if (comments == null) {
                    comments = commentsNew;
                }
            }
        }
        return comments;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 249603048)
    public synchronized void resetComments() {
        comments = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1083172549)
    public List<Rate> getRates() {
        if (rates == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RateDao targetDao = daoSession.getRateDao();
            List<Rate> ratesNew = targetDao._querySights_Rates(id);
            synchronized (this) {
                if (rates == null) {
                    rates = ratesNew;
                }
            }
        }
        return rates;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1397501099)
    public synchronized void resetRates() {
        rates = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 389787630)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSightsDao() : null;
    }

    public Double getResortScore() {
        return this.resortScore;
    }

    public void setResortScore(Double resortScore) {
        this.resortScore = resortScore;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }



}
