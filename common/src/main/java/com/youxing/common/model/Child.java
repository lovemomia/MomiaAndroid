package com.youxing.common.model;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jun Deng on 15/8/10.
 */
public class Child {

    private long id;
    private String name;
    private String sex;
    private String birthday;
    private String avatar;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAge() {
        if (TextUtils.isEmpty(getBirthday())) {
            return "？岁";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = format.parse(getBirthday());
        } catch (ParseException e) {
            return null;
        }

        Calendar childCalendar = Calendar.getInstance();
        childCalendar.setTime(date);
        int childYear = childCalendar.get(Calendar.YEAR);
        int childMonth = childCalendar.get(Calendar.MONTH);
        int childDay = childCalendar.get(Calendar.DAY_OF_MONTH);

        Calendar now = Calendar.getInstance();
        int nowYear = now.get(Calendar.YEAR);
        int nowMonth = now.get(Calendar.MONTH);
        int nowDay = childCalendar.get(Calendar.DAY_OF_MONTH);

        StringBuilder sb = new StringBuilder();
        if (nowYear < childYear || (nowYear == childYear && nowMonth < childMonth)) {
            return sb.append("还未出生").toString();
        }

        int age = nowYear - childYear - 1;
        if ((nowMonth > childMonth) || (nowMonth == childMonth && nowDay >= childDay)) {
            age++;
        }

        if (age > 0) {
            if (age == 1 && nowMonth < childMonth) {
                int month = 12 + nowMonth - childMonth;
                return sb.append(month + "个月").toString();
            } else {
                return sb.append(age + "岁").toString();
            }
        }

        // 几个月
        int month = nowMonth - childMonth;
        if (nowMonth < childMonth) {
            month = 12 - childMonth + nowMonth;
        } else if (nowMonth == childMonth) {
            month = 1;
        }
        return sb.append(month + "个月").toString();
    }
}
