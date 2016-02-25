package com.youxing.duola.model;

import java.util.List;

/**
 * Created by Jun Deng on 16/2/22.
 */
public class Course {

    private long id;
    private String cover;
    private String title; //标题
    private float price; //价格
    private String age; //年龄
    private int joined; //参加人数
    private String scheduler; //场次日期
    private String region; //地区
    private boolean insurance; //红包
    private long bookingId; //预约id
    private String subject;
    private long subjectId;
    private int type; // 0:一般课程，1:公益课， 2:推荐课程
    private String notice;

    private List<String> imgs;
    private String goal;
    private CoursePlace place;

    private String tips; //提示
    private String institution; //合作机构
    private boolean commented; //是否已评论过
    private boolean buyable;
    private int status; //1 可购买  2 不可购买

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getJoined() {
        return joined;
    }

    public void setJoined(int joined) {
        this.joined = joined;
    }

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isInsurance() {
        return insurance;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

    public long getBookingId() {
        return bookingId;
    }

    public void setBookingId(long bookingId) {
        this.bookingId = bookingId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public CoursePlace getPlace() {
        return place;
    }

    public void setPlace(CoursePlace place) {
        this.place = place;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public boolean isCommented() {
        return commented;
    }

    public void setCommented(boolean commented) {
        this.commented = commented;
    }

    public boolean isBuyable() {
        return buyable;
    }

    public void setBuyable(boolean buyable) {
        this.buyable = buyable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class CoursePlace {
        private long id;
        private String name;
        private String address;
        private float lng;
        private float lat;
        private String scheduler;

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

        public String getScheduler() {
            return scheduler;
        }

        public void setScheduler(String scheduler) {
            this.scheduler = scheduler;
        }
    }
}