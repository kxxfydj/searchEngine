package com.kxxfydj.entity;

import java.util.Date;

/**
 * Created by kxxfydj on 2018/3/31.
 */
public class CodeContent {
    private String path;

    private String body;

    private Date updateTime;

    private Date addTime;

    private int codeInfoId;

    private String fatherPath;

    private boolean enabled;

    public int getCodeInfoId() {
        return codeInfoId;
    }

    public void setCodeInfoId(int codeInfoId) {
        this.codeInfoId = codeInfoId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getFatherPath() {
        return fatherPath;
    }

    public void setFatherPath(String fatherPath) {
        this.fatherPath = fatherPath;
    }
}
