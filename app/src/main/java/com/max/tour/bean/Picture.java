package com.max.tour.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 *
 */
@Entity
public class Picture {

    @Id(autoincrement = true)
    private Long id;
    private Long sightId;
    private String path;
    @Generated(hash = 452436703)
    public Picture(Long id, Long sightId, String path) {
        this.id = id;
        this.sightId = sightId;
        this.path = path;
    }
    @Generated(hash = 1602548376)
    public Picture() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getSightId() {
        return this.sightId;
    }
    public void setSightId(Long sightId) {
        this.sightId = sightId;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }

}
