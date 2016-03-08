package com.youxing.duola.model;

import java.util.List;

/**
 * Created by Jun Deng on 16/3/8.
 */
public class ReviewList {

    private List<Review> list;
    private long nextIndex;
    private long totalCount;

    public List<Review> getList() {
        return list;
    }

    public void setList(List<Review> list) {
        this.list = list;
    }

    public long getNextIndex() {
        return nextIndex;
    }

    public void setNextIndex(long nextIndex) {
        this.nextIndex = nextIndex;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public static class Review {

        private long userId;
        private String nickName;
        private String avatar;
        private List<String> children;
        private List<ReviewChild> childrenDetail;

        private int star;
        private String addTime;
        private String content;
        private List<String> imgs;
        private List<String> largeImgs;

        private long courseId;
        private String courseTitle;

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public List<String> getChildren() {
            return children;
        }

        public void setChildren(List<String> children) {
            this.children = children;
        }

        public List<ReviewChild> getChildrenDetail() {
            return childrenDetail;
        }

        public void setChildrenDetail(List<ReviewChild> childrenDetail) {
            this.childrenDetail = childrenDetail;
        }

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<String> getImgs() {
            return imgs;
        }

        public void setImgs(List<String> imgs) {
            this.imgs = imgs;
        }

        public List<String> getLargeImgs() {
            return largeImgs;
        }

        public void setLargeImgs(List<String> largeImgs) {
            this.largeImgs = largeImgs;
        }

        public long getCourseId() {
            return courseId;
        }

        public void setCourseId(long courseId) {
            this.courseId = courseId;
        }

        public String getCourseTitle() {
            return courseTitle;
        }

        public void setCourseTitle(String courseTitle) {
            this.courseTitle = courseTitle;
        }
    }

    public static class ReviewChild {

        private String age;
        private String name;
        private String sex;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}
