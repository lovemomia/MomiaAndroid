package com.youxing.duola.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 15/6/17.
 * Refactor on 16/3/10 v1.4
 */
public class HomeModel extends BaseModel {

    private HomeData data;

    public HomeData getData() {
        return data;
    }

    public void setData(HomeData data) {
        this.data = data;
    }

    public static class HomeData {
        private List<HomeBanner> banners;
        private String eventsTitle;
        private List<HomeEvent> events;
        private List<HomeSubject> subjects;
        private List<HomeTopic> topics;
        private CourseListModel.CourseList courses;

        public List<HomeBanner> getBanners() {
            return banners;
        }

        public void setBanners(List<HomeBanner> banners) {
            this.banners = banners;
        }

        public String getEventsTitle() {
            return eventsTitle;
        }

        public void setEventsTitle(String eventsTitle) {
            this.eventsTitle = eventsTitle;
        }

        public List<HomeEvent> getEvents() {
            return events;
        }

        public void setEvents(List<HomeEvent> events) {
            this.events = events;
        }

        public List<HomeSubject> getSubjects() {
            return subjects;
        }

        public void setSubjects(List<HomeSubject> subjects) {
            this.subjects = subjects;
        }

        public List<HomeTopic> getTopics() {
            return topics;
        }

        public void setTopics(List<HomeTopic> topics) {
            this.topics = topics;
        }

        public CourseListModel.CourseList getCourses() {
            return courses;
        }

        public void setCourses(CourseListModel.CourseList courses) {
            this.courses = courses;
        }
    }

    public static class HomeBanner {
        private String cover;
        private String action;

        public String getAction() {
            return action;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

    public static class HomeEvent {
        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public int getPlatform() {
            return platform;
        }

        public void setPlatform(int platform) {
            this.platform = platform;
        }

        private String action;
        private String desc;
        private String img;
        private String title;
        private String version;
        private int platform;


    }

    public static class HomeSubject {
        private long id;
        private String cover;
        private String age;
        private int joined;
        private double price;
        private String title;
        private String subTitle;
        private String coursesTitle;
        private List<Course> courses;

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

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getCoursesTitle() {
            return coursesTitle;
        }

        public void setCoursesTitle(String coursesTitle) {
            this.coursesTitle = coursesTitle;
        }

        public List<Course> getCourses() {
            return courses;
        }

        public void setCourses(List<Course> courses) {
            this.courses = courses;
        }
    }

    public static class HomeTopic {
        private long id;
        private String cover;
        private int joined;
        private String title;
        private String subTitle;

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

        public int getJoined() {
            return joined;
        }

        public void setJoined(int joined) {
            this.joined = joined;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }
    }
}
