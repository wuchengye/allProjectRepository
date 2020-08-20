package com.jingdong.webmagic.RESTful;

public enum ResultCode {
    /* 成功状态码 */
    SUCCESS(1, "成功"),
    /* 失败状态码 */
    FAILURE(0, "失败"),

    USER_NOT_EXIST(1000,"用户不存在"),
    USER_PASSWORD_ERROR(1001,"密码或验证码错误"),
    USER_EXIST(1002,"用户存在"),
    CODE_NULL(1003,"验证码为空"),
    CODE_TIMEOUT(1004,"验证码过期");


    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return code;
    }

    public String message() {
        return message;
    }
}
