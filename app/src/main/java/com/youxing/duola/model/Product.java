package com.youxing.duola.model;

/**
 * Created by Jun Deng on 15/6/17.
 */
public class Product {

    private long id;
    private String cover;
    private int joined;
    private double price;
    private String title;
    private String scheduler;

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
}
