package com.youxing.duola.model;

import com.youxing.common.model.BaseModel;

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
        private CourseListModel.CourseList courses;
        private ReviewList comments;

        public Subject getSubject() {
            return subject;
        }

        public void setSubject(Subject subject) {
            this.subject = subject;
        }

        public CourseListModel.CourseList getCourses() {
            return courses;
        }

        public void setCourses(CourseListModel.CourseList courses) {
            this.courses = courses;
        }

        public ReviewList getComments() {
            return comments;
        }

        public void setComments(ReviewList comments) {
            this.comments = comments;
        }
    }
}
