package com.meridian.dateout.reminder;

/**
 * Created by Ansal on 19-Mar-18.
 */

public class PastModel {
    String id,event_type,event_name,event_date,event_time,event_details,event_month,date,unique_id;

    public String getEvent_month() {
        return event_month;
    }

    public void setEvent_month(String event_month) {
        this.event_month = event_month;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getEvent_details() {
        return event_details;
    }

    public void setEvent_details(String event_details) {
        this.event_details = event_details;
    }

    public void setevent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getevent_date() {
        return event_date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String getdate() {
        return date;
    }

    public void setunique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getunique_id() {
        return unique_id;
    }
}
