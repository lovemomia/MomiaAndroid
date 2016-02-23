package com.youxing.duola.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 16/2/18.
 */
public class BookingSubjectListModel extends BaseModel {

    private BookingSubjectListData data;

    public BookingSubjectListData getData() {
        return data;
    }

    public void setData(BookingSubjectListData data) {
        this.data = data;
    }

    public static class BookingSubjectListData {
        private long nextIndex;
        private long totalCount;
        private List<BookingSubject> list;

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

        public List<BookingSubject> getList() {
            return list;
        }

        public void setList(List<BookingSubject> list) {
            this.list = list;
        }
    }

    public static class BookingSubject {
        private long packageId;
        private long subjectId;
        private String title;
        private String cover;
        private long bookableCourseCount;
        private String expireTime;
        private long courseId;

        public long getPackageId() {
            return packageId;
        }

        public void setPackageId(long packageId) {
            this.packageId = packageId;
        }

        public long getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(long subjectId) {
            this.subjectId = subjectId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public long getBookableCourseCount() {
            return bookableCourseCount;
        }

        public void setBookableCourseCount(long bookableCourseCount) {
            this.bookableCourseCount = bookableCourseCount;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public long getCourseId() {
            return courseId;
        }

        public void setCourseId(long courseId) {
            this.courseId = courseId;
        }
    }
}
