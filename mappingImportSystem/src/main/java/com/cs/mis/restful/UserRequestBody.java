package com.cs.mis.restful;

import io.swagger.annotations.ApiModel;

/**
 * @author wcy
 */

public class UserRequestBody {
    private String userAccount = "";
    private String userPwd = "";
    private String codeKey = "";
    private String codeValue = "";
    private String rsaKey = "";
    private String userOldPwd = "";

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getCodeKey() {
        return codeKey;
    }

    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getRsaKey() {
        return rsaKey;
    }

    public void setRsaKey(String rsaKey) {
        this.rsaKey = rsaKey;
    }

    public String getUserOldPwd() {
        return userOldPwd;
    }

    public void setUserOldPwd(String userOldPwd) {
        this.userOldPwd = userOldPwd;
    }
}
