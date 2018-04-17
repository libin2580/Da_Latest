package com.meridian.dateout.model;

/**
 * Created by user1 on 05-Dec-16.
 */
public class MyData {
    private String title, genre, year, issue, country;

    public MyData() {
    }

    public MyData(String title, String genre, String year, String issue, String country) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.issue = issue;
        this.country = country;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
