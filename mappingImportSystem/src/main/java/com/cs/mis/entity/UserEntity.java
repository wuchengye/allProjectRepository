package com.cs.mis.entity;


/**
 * @author wcy
 */

public class UserEntity {

    public static final int USERSTATUS_VALID = 1;
    public static final int USERSTATUS_INVALID = 0;
    public static final int USERTYPE_MANAGER = 1;
    public static final int USERTYPE_COMMON = 0;
    public static final String DEFAULT_PWD = "06127a02d497eecc6f17c3ec94b6c79d";

    private Long userId;
    private String userAccount;
    private String userName;
    private String userPwd;
    private int userType;
    private int userStatus;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }
}
