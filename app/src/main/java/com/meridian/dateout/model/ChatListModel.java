package com.meridian.dateout.model;

/**
 * Created by user 1 on 05-12-2016.
 */

public class ChatListModel {
    private String cht_topic;
    private String cht_description;
    private  String id;
    private  String category_name;

    public String getCht_topic() {
        return cht_topic;
    }

    public void setCht_topic(String cht_topic) {
        this.cht_topic = cht_topic;
    }

    public String getCht_description() {
        return cht_description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public void setCht_description(String cht_description) {
        this.cht_description = cht_description;


    }
}
