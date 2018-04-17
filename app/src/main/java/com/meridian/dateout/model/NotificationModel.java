package com.meridian.dateout.model;

/**
 * Created by user 1 on 06-12-2016.
 */

public class NotificationModel {
    private String not_event,deal_id;
    private String not_loc;

    public String getDeal_id() {
        return deal_id;
    }

    public void setDeal_id(String deal_id) {
        this.deal_id = deal_id;
    }

    public String getNot_dealtme() {
        return not_dealtme;
    }

    public void setNot_dealtme(String not_dealtme) {
        this.not_dealtme = not_dealtme;
    }

    private String not_dealtme;

    public String getNot_event() {
        return not_event;
    }

    public void setNot_event(String not_event) {
        this.not_event = not_event;
    }

    public String getNot_loc() {
        return not_loc;
    }

    public void setNot_loc(String not_loc) {
        this.not_loc = not_loc;
    }

    public String getNot_time() {
        return not_time;
    }

    public void setNot_time(String not_time) {
        this.not_time = not_time;
    }

    private String not_time;
}
