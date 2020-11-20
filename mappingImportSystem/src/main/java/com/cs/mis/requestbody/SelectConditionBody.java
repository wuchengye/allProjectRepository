package com.cs.mis.requestbody;

import com.cs.mis.utils.DateUtil;

/**
 * @author wcy
 */

public class SelectConditionBody {
    private String center;
    private String support;
    private String jobNum;
    private String platformNum;
    private String date = DateUtil.getDateOfToday();
    private String name;

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getPlatformNum() {
        return platformNum;
    }

    public void setPlatformNum(String platformNum) {
        this.platformNum = platformNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
