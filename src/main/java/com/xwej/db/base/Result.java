package com.xwej.db.base;



import java.io.Serializable;

import com.xwej.db.cache.ClientErrCode;

public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int SUCC = 0;
    public static final int FAILED = 1;

    private int statusCode = 0;
    private String msg;
    private T data;

    public Result() {

    }

    public Result(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public Result(int statusCode, String msg, T data) {
        this.statusCode = statusCode;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> FailedResult(ClientErrCode clientErrCode) {
        return new Result<T>(clientErrCode.getCode(), clientErrCode.getDesc());
    }

    public static <T> Result<T> FailedResult(ClientErrCode clientErrCode, T t) {
        return new Result<T>(clientErrCode.getCode(), clientErrCode.getDesc(), t);
    }

    public static <T> Result<T> SuccResult() {
        return new Result<T>(SUCC, "成功");
    }

    public static <T> Result<T> SuccResult(String msg) {
        return new Result(SUCC, msg);
    }

    public static <T> Result<T> SuccResult(T data) {
        return new Result<T>(SUCC, "成功", data);
    }

    public static <T> Result<T> FailedResult() {
        return new Result<T>(FAILED, "失败");
    }

    public static <T> Result<T> FailedResult(String msg) {
        return new Result<T>(FAILED, msg);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
