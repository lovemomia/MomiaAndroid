package com.youxing.duola.model;

/**
 * Created by Jun Deng on 15/6/17.
 */
public class Product {

    private Long id;
    private String cover;
    private int joined;
    private double price;
    private String title;
    private String scheduler;
    private String address;
    private String poi;

    public long getId() {
        return id;
    }

    public String getCover() {
        return cover;
    }

    public int getJoined() {
        return joined;
    }

    public double getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getScheduler() {
        return scheduler;
    }

    public String getAddress() {
        return address;
    }

    public String getPoi() {
        return poi;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setJoined(int joined) {
        this.joined = joined;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPoi(String poi) {
        this.poi = poi;
    }
}
