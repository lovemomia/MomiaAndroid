package com.youxing.duola.model;

/**
 * Created by Jun Deng on 15/8/11.
 */
public class Sku {

    private String desc;
    private int limit;

    private long id;
    private long subjectId;
    private long courseId;
    private double price;

    private int count; //提交订单用的，记录个数

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}
