package com.youxing.duola.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 16/3/9.
 */
public class SubjectDetailModel extends BaseModel {

    private SubjectDetailData data;

    public SubjectDetailData getData() {
        return data;
    }

    public void setData(SubjectDetailData data) {
        this.data = data;
    }

    public static class SubjectDetailData {

        private Subject subject;
        private ReviewList comments;
        private List<Course> courses;
        private List<Course> newCourses;

        public Subject getSubject() {
            return subject;
        }

        public void setSubject(Subject subject) {
            this.subject = subject;
        }

        public ReviewList getComments() {
            return comments;
        }

        public void setComments(ReviewList comments) {
            this.comments = comments;
        }

        public List<Course> getCourses() {
            return courses;
        }

        public void setCourses(List<Course> courses) {
            this.courses = courses;
        }

        public List<Course> getNewCourses() {
            return newCourses;
        }

        public void setNewCourses(List<Course> newCourses) {
            this.newCourses = newCourses;
        }
    }
}
