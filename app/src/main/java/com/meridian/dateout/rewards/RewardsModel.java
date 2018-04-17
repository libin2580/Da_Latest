package com.meridian.dateout.rewards;

/**
 * Created by Anvin on 10/7/2017.
 */

public class RewardsModel {

    String reward_text,reward_image,reward_des,rewrd_title,rewrd_startdate,end_date,earned_for,rewrd_date,rewrd_type,redeem_points,id,rewrd_type_image;

    public String getRewrd_date() {
        return rewrd_date;
    }

    public String getRewrd_type_image() {
        return rewrd_type_image;
    }

    public void setRewrd_type_image(String rewrd_type_image) {
        this.rewrd_type_image = rewrd_type_image;
    }

    public String getRewrd_type() {
        return rewrd_type;
    }

    public void setRewrd_type(String rewrd_type) {
        this.rewrd_type = rewrd_type;
    }

    public String getRedeem_points() {
        return redeem_points;
    }

    public void setRedeem_points(String redeem_points) {
        this.redeem_points = redeem_points;
    }

    public void setRewrd_date(String rewrd_date) {
        this.rewrd_date = rewrd_date;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReward_des() {
        return reward_des;
    }

    public void setReward_des(String reward_des) {
        this.reward_des = reward_des;
    }

    public String getRewrd_title() {
        return rewrd_title;
    }

    public void setRewrd_title(String rewrd_title) {
        this.rewrd_title = rewrd_title;
    }

    public String getRewrd_startdate() {
        return rewrd_startdate;
    }

    public void setRewrd_startdate(String rewrd_startdate) {
        this.rewrd_startdate = rewrd_startdate;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getEarned_for() {
        return earned_for;
    }

    public void setEarned_for(String earned_for) {
        this.earned_for = earned_for;
    }

    public String getReward_text() {
        return reward_text;
    }

    public void setReward_text(String reward_text) {
        this.reward_text = reward_text;
    }

    public String getReward_image() {
        return reward_image;
    }

    public void setReward_image(String reward_image) {
        this.reward_image = reward_image;
    }
}
