package com.youxing.duola.model;

import android.os.Parcel;
import android.os.Parcelable;

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

    public static class CourseSku implements Parcelable {
        private long id;
        private CourseSkuPlace place;
        private int stock;
        private String time;
        private boolean closed;

        public CourseSku() {

        }

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(id);
            dest.writeParcelable(place, flags);
            dest.writeInt(stock);
            dest.writeString(time);
            dest.writeInt(closed ? 0 : 1);
        }

        public CourseSku(Parcel parcel) {
            id = parcel.readLong();
            place = parcel.readParcelable(CourseSkuPlace.class.getClassLoader());
            stock = parcel.readInt();
            time = parcel.readString();
            closed = parcel.readInt() == 0;
        }

        public static final Parcelable.Creator<CourseSku> CREATOR = new Parcelable.Creator<CourseSku>() {
            @Override
            public CourseSku createFromParcel(Parcel source) {
                return new CourseSku(source);
            }

            @Override
            public CourseSku[] newArray(int size) {
                return new CourseSku[size];
            }
        };
    }

    public static class CourseSkuPlace implements Parcelable {
        private long id;
        private String name;
        private String address;
        private float lng;
        private float lat;

        public CourseSkuPlace() {

        }

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(id);
            dest.writeString(name);
            dest.writeString(address);
            dest.writeFloat(lng);
            dest.writeFloat(lat);
        }

        public CourseSkuPlace(Parcel parcel) {
            id = parcel.readLong();
            name = parcel.readString();
            address =  parcel.readString();
            lng = parcel.readFloat();
            lat = parcel.readFloat();
        }

        public static final Parcelable.Creator<CourseSkuPlace> CREATOR = new Parcelable.Creator<CourseSkuPlace>() {
            @Override
            public CourseSkuPlace createFromParcel(Parcel source) {
                return new CourseSkuPlace(source);
            }
            @Override
            public CourseSkuPlace[] newArray(int size) {
                return new CourseSkuPlace[size];
            }
        };
    }
}
