package com.max.tour.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 评论
 */
@Entity
public class Comment implements Serializable {

    public static final long  serialVersionUID = 3L;

    @Id(autoincrement = true)
    private Long id;
    private Long userId;
    private Long sightId;
    @Property
    private String userIcon;
    @Property
    private Date addtime;
    @Property
    private String content;
    @Property
    private String reply;
    @Generated(hash = 1902367467)
    public Comment(Long id, Long userId, Long sightId, String userIcon,
            Date addtime, String content, String reply) {
        this.id = id;
        this.userId = userId;
        this.sightId = sightId;
        this.userIcon = userIcon;
        this.addtime = addtime;
        this.content = content;
        this.reply = reply;
    }
    @Generated(hash = 1669165771)
    public Comment() {
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
    public Long getSightId() {
        return this.sightId;
    }
    public void setSightId(Long sightId) {
        this.sightId = sightId;
    }
    public String getUserIcon() {
        return this.userIcon;
    }
    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }
    public Date getAddtime() {
        return this.addtime;
    }
    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getReply() {
        return this.reply;
    }
    public void setReply(String reply) {
        this.reply = reply;
    }


}
