package com.gcl.navigation.exception;

public enum BusinessExceptionCode {

    USER_LOGIN_EMAIL_EXIST("邮箱已注册"),
    LOGIN_USER_ERROR("用户名不存在或密码错误"),
    WEBSITE_SAVE_EXIST("网站已添加"),
    WEBSITE_EMPTY("网站信息为空"),
    ;

    private String desc;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
