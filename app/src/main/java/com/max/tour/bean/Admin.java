package com.max.tour.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class Admin {


    @Id(autoincrement = true)
    private Long id;
    @Property
    private String adminname;
    @Property
    private String adminjiassword;
    @Property
    private String adminLevel;
    @Property
    private Date addTime;
    @Generated(hash = 1192796849)
    public Admin(Long id, String adminname, String adminjiassword,
            String adminLevel, Date addTime) {
        this.id = id;
        this.adminname = adminname;
        this.adminjiassword = adminjiassword;
        this.adminLevel = adminLevel;
        this.addTime = addTime;
    }
    @Generated(hash = 1708792177)
    public Admin() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAdminname() {
        return this.adminname;
    }
    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }
    public String getAdminjiassword() {
        return this.adminjiassword;
    }
    public void setAdminjiassword(String adminjiassword) {
        this.adminjiassword = adminjiassword;
    }
    public String getAdminLevel() {
        return this.adminLevel;
    }
    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }
    public Date getAddTime() {
        return this.addTime;
    }
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }


}
