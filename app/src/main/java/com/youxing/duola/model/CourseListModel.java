package com.youxing.duola.model;

import com.youxing.common.model.BaseModel;

import java.util.List;

/**
 * Created by Jun Deng on 16/2/22.
 */
public class CourseListModel extends BaseModel {

    private CourseListData data;

    public CourseListData getData() {
        return data;
    }

    public void setData(CourseListData data) {
        this.data = data;
    }

    public static class CourseListData {
        private List<Filter> ages;
        private List<Filter> sorts;
        private int currentAge;
        private int currentSort;
        private CourseList courses;

        public List<Filter> getAges() {
            return ages;
        }

        public void setAges(List<Filter> ages) {
            this.ages = ages;
        }

        public List<Filter> getSorts() {
            return sorts;
        }

        public void setSorts(List<Filter> sorts) {
            this.sorts = sorts;
        }

        public int getCurrentAge() {
            return currentAge;
        }

        public void setCurrentAge(int currentAge) {
            this.currentAge = currentAge;
        }

        public int getCurrentSort() {
            return currentSort;
        }

        public void setCurrentSort(int currentSort) {
            this.currentSort = currentSort;
        }

        public CourseList getCourses() {
            return courses;
        }

        public void setCourses(CourseList courses) {
            this.courses = courses;
        }
    }

    public static class CourseList {
        private List<Course> list;
        private long nextIndex;
        private long totalCount;

        public List<Course> getList() {
            return list;
        }

        public void setList(List<Course> list) {
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
    }

    public static class Filter {
        private long id;
        private String text;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
