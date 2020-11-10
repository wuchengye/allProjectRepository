package com.cs.mis.entity;

import java.util.List;

/**
 * @author wcy
 */

public class ExcelDataEntity {
    private String center;
    private String support;
    private String platformNum;
    private String name;
    private String group;
    private String positionName;
    private String memberType;
    private String standardPositionName;
    private String beginTime;
    private String endTime;
    private String remark;
    private List<String> jonNum;

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

    public String getPlatformNum() {
        return platformNum;
    }

    public void setPlatformNum(String platformNum) {
        this.platformNum = platformNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getStandardPositionName() {
        return standardPositionName;
    }

    public void setStandardPositionName(String standardPositionName) {
        this.standardPositionName = standardPositionName;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getJonNum() {
        return jonNum;
    }

    public void setJonNum(List<String> jonNum) {
        this.jonNum = jonNum;
    }
}
