package com.youxing.duola.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jun Deng on 15/8/25.
 */
public class Order implements Parcelable {

    private long id;
    private long subjectId;
    private long courseId;

    private long count;
    private double totalFee;
    private double payedFee;
    private int payType; // 1 支付宝  2 微信
    private int status; // 2未支付 3已付款 4已使用 5待退款 6已退款 7 申请通过 8 返现中 9 已返现
    private int bookingStatus; // 1 待预约，2 待上课，3 已上课
    private boolean canRefund; // 是否能够退款 只有在“已付款”或“已返现”状态下才有意义

    private String couponDesc;
    private String title;
    private String addTime;
    private String cover;

    private GroupInfo groupInfo;

    public Order() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(int bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getCouponDesc() {
        return couponDesc;
    }

    public void setCouponDesc(String couponDesc) {
        this.couponDesc = couponDesc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public double getPayedFee() {
        return payedFee;
    }

    public void setPayedFee(double payedFee) {
        this.payedFee = payedFee;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public boolean isCanRefund() {
        return canRefund;
    }

    public void setCanRefund(boolean canRefund) {
        this.canRefund = canRefund;
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(subjectId);
        dest.writeLong(courseId);

        dest.writeLong(count);
        dest.writeDouble(totalFee);
        dest.writeDouble(payedFee);
        dest.writeInt(payType);
        dest.writeInt(status);
        dest.writeInt(bookingStatus);
        dest.writeInt(canRefund ? 0 : 1);

        dest.writeString(couponDesc);
        dest.writeString(title);
        dest.writeString(addTime);
        dest.writeString(cover);

        dest.writeParcelable(groupInfo, flags);
    }

    public Order(Parcel parcel) {
        id = parcel.readLong();
        subjectId = parcel.readLong();
        courseId = parcel.readLong();
        count = parcel.readLong();
        totalFee = parcel.readDouble();
        payedFee = parcel.readDouble();
        payType = parcel.readInt();
        status = parcel.readInt();
        bookingStatus = parcel.readInt();
        canRefund = parcel.readInt() == 0;

        couponDesc = parcel.readString();
        title = parcel.readString();
        addTime = parcel.readString();
        cover = parcel.readString();

        groupInfo = parcel.readParcelable(GroupInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public static class GroupInfo implements Parcelable {
        private int joinedCount;
        private double returnFee;
        private String startTime;
        private String endTime;
        private boolean successful;

        public int getJoinedCount() {
            return joinedCount;
        }

        public void setJoinedCount(int joinedCount) {
            this.joinedCount = joinedCount;
        }

        public double getReturnFee() {
            return returnFee;
        }

        public void setReturnFee(double returnFee) {
            this.returnFee = returnFee;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public boolean isSuccessful() {
            return successful;
        }

        public void setSuccessful(boolean successful) {
            this.successful = successful;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(joinedCount);
            dest.writeDouble(returnFee);
            dest.writeString(startTime);
            dest.writeString(endTime);
            dest.writeInt(successful ? 0 : 1);
        }

        public GroupInfo(Parcel parcel) {
            joinedCount = parcel.readInt();
            returnFee = parcel.readDouble();
            startTime = parcel.readString();
            endTime = parcel.readString();
            successful = parcel.readInt() == 0;
        }

        public static final Parcelable.Creator<GroupInfo> CREATOR = new Parcelable.Creator<GroupInfo>() {
            @Override
            public GroupInfo createFromParcel(Parcel source) {
                return new GroupInfo(source);
            }

            @Override
            public GroupInfo[] newArray(int size) {
                return new GroupInfo[size];
            }
        };
    }
}
