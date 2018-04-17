package com.meridian.dateout.nearme.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Anvin on 8/28/2017.
 */

public class GoogleMapModel {
    List<List<HashMap<String, String>>> routes;
    String distance,duration;
    String start_address,end_address,start_lat,start_lng,end_lat,end_lng;
    ArrayList<String> html_instructions;

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public String getStart_lat() {
        return start_lat;
    }

    public void setStart_lat(String start_lat) {
        this.start_lat = start_lat;
    }

    public String getStart_lng() {
        return start_lng;
    }

    public void setStart_lng(String start_lng) {
        this.start_lng = start_lng;
    }

    public String getEnd_lat() {
        return end_lat;
    }

    public void setEnd_lat(String end_lat) {
        this.end_lat = end_lat;
    }

    public String getEnd_lng() {
        return end_lng;
    }

    public void setEnd_lng(String end_lng) {
        this.end_lng = end_lng;
    }

    public ArrayList<String> getHtml_instructions() {
        return html_instructions;
    }

    public void setHtml_instructions(ArrayList<String> html_instructions) {
        this.html_instructions = html_instructions;
    }

    public List<List<HashMap<String, String>>> getRoutes() {
        return routes;
    }

    public void setRoutes(List<List<HashMap<String, String>>> routes) {
        this.routes = routes;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
