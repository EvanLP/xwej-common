package com.xwej.db.cache;

/**
 * Created by lmh on 2017/7/3.
 * 客户端全局错误码
 */
public enum ClientErrCode {

    WECHAT_LOGIN_FAILD(301, "用户登录超时"),
    PARAM_ERR(400, "参数错误，请检查参数"),
    SERVER_ERR(500, "服务器繁忙，请稍后再试");

    private int code;
    private String desc;

    ClientErrCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
