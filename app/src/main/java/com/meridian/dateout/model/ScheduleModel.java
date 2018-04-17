package com.meridian.dateout.model;

import java.util.ArrayList;

/**
 * Created by user 1 on 14-11-2016.
 */

public class ScheduleModel {
    private String date;
    private String month;
    private String year;
    private ArrayList<String> time;
    private String str_time;
    private String dayscount;

    public String getStr_time() {
        return str_time;
    }

    public String getDayscount() {
        return dayscount;
    }

    public void setDayscount(String dayscount) {
        this.dayscount = dayscount;
    }

    public void setStr_time(String str_time) {
        this.str_time = str_time;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public ArrayList<String> getTime() {
        return time;
    }

    public void setTime(ArrayList<String> time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    private String day;
}
