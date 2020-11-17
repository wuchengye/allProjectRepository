package com.cs.mis.entity;

import io.netty.util.internal.StringUtil;
import java.util.*;

/**
 * @author wcy
 */

public class ExcelDataEntity {

    public static final int EXCEL_CELL_SIZE = 21;
    public static final String EXCEL_DATE_PATTERN = "^(?:(?!0000)[0-9]{4}(?:(?:0[1-9]|1[0-2])(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])(?:29|30)|(?:0[13578]|1[02])31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)0229)$";
    public static final List<String> CENTER_ALL = new ArrayList<String>(){
        {
            add("省统众包");add("广州中心");add("深圳中心");add("东莞中心");add("佛山中心");add("汕头中心");add("江门中心");
        }
    };
    public static final List<String> SUPPORT_ALL = new ArrayList<String>(){
        {
            add("广东科慧平台");add("广州点动平台");add("深圳润迅平台");add("鸿联九五平台");add("博岳平台");
            add("泰盈平台");add("中通信息平台");add("北京众信佳平台");add("自营");
        }
    };
    public static final List<String> POSITION_NAME_ALL = new ArrayList<String>(){
        {
            add("呼入客服代表");add("班长");add("培训师");add("综援");
        }
    };
    public static final List<String> MEMBER_TYPE_ALL = new ArrayList<String>(){
        {
            add("新会员");add("成熟会员");add("自营");
        }
    };

    private String importTime;
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
    private String jobNums;
    private List<String> jobNumList;

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(String importTime) {
        this.importTime = importTime;
    }

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

    public List<String> getJobNumList() {
        if(jobNumList == null){
            if(StringUtil.isNullOrEmpty(jobNums)){
                return null;
            }else {
                String[] arg = jobNums.split(",");
                jobNumList = new ArrayList<>();
                Collections.addAll(jobNumList,arg);
                return jobNumList;
            }
        }else {
            return jobNumList;
        }
    }

    public void setJobNumList(List<String> jobNumList) {
        this.jobNumList = null;
        if(jobNumList == null || jobNumList.size() == 0){
            jobNums = jobNumList == null ? null : "";
        }else {
            StringBuffer sb = new StringBuffer();
            for (String num : jobNumList){
                sb.append(num);
                sb.append(",");
            }
            jobNums = sb.toString().substring(0,sb.toString().length()-1);
        }
    }

    @Override
    public String toString() {
        String A = "&&";
        return center + A + support + A + platformNum + A + name + A + group + A + positionName + A + memberType + A +
                standardPositionName + A + beginTime + A + endTime + A + remark + A + jobNums.replace(",",A);
    }
}
