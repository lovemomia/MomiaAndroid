package com.youxing.common.model;

/**
 * Created by Jun Deng on 15/6/3.
 */
public class BaseModel {

    private int errno;
    private String errmsg;
    private long time;

    public BaseModel() {
        this(0, "success");
    }

    public BaseModel(int errno, String errmsg) {
        this.errno = errno;
        this.errmsg = errmsg;
    }

    public int getErrno() {
        return errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public long getTime() {
        return time;
    }
}
