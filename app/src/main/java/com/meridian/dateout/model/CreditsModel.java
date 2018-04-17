package com.meridian.dateout.model;

/**
 * Created by user 1 on 09-02-2017.
 */

public class CreditsModel {
    private  String id;
    private  String user_id;
    private  String points;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    private  String bk_id;
    private  String title;
    private  String bk_date;
    private  String total_price;
    private  String country;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getBk_id() {
        return bk_id;
    }

    public void setBk_id(String bk_id) {
        this.bk_id = bk_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBk_date() {
        return bk_date;
    }

    public void setBk_date(String bk_date) {
        this.bk_date = bk_date;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }
}
