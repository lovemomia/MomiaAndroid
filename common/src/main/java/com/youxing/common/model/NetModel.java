package com.youxing.common.model;

/**
 * Created by Jun Deng on 15/6/3.
 */
public class NetModel {

    private int errno;
    private String errmsg;

    public NetModel() {
        this(0, "success");
    }

    public NetModel(int errno, String errmsg) {
        this.errno = errno;
        this.errmsg = errmsg;
    }

    public int getErrno() {
        return errno;
    }

    public String getErrmsg() {
        return errmsg;
    }
}
