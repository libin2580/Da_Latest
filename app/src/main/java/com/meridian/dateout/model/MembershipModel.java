package com.meridian.dateout.model;

/**
 * Created by user1 on 23-Oct-17.
 */

public class MembershipModel {
    String title,image,discription,point_stage,points_away,status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getPoint_stage() {
        return point_stage;
    }

    public void setPoint_stage(String point_stage) {
        this.point_stage = point_stage;
    }

    public String getPoints_away() {
        return points_away;
    }

    public void setPoints_away(String points_away) {
        this.points_away = points_away;
    }
}
