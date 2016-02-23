package com.youxing.duola.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 16/2/23.
 */
public class CourseSkuListModel extends BaseModel {

    private List<DateSkuList> data;

    public List<DateSkuList> getData() {
        return data;
    }

    public void setData(List<DateSkuList> data) {
        this.data = data;
    }

    public static class DateSkuList {
        private String date;
        private List<CourseSku> skus;
        private boolean isShowMore;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<CourseSku> getSkus() {
            return skus;
        }

        public void setSkus(List<CourseSku> skus) {
            this.skus = skus;
        }

        public boolean isShowMore() {
            return isShowMore;
        }

        public void setShowMore(boolean showMore) {
            isShowMore = showMore;
        }
    }

    public static class CourseSku {
        private long id;
        private CourseSkuPlace place;
        private int stock;
        private String time;
        private boolean closed;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public CourseSkuPlace getPlace() {
            return place;
        }

        public void setPlace(CourseSkuPlace place) {
            this.place = place;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public boolean isClosed() {
            return closed;
        }

        public void setClosed(boolean closed) {
            this.closed = closed;
        }
    }

    public static class CourseSkuPlace {
        private long id;
        private String name;
        private String address;
        private float lng;
        private float lat;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public float getLng() {
            return lng;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }

        public float getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }
    }
}
