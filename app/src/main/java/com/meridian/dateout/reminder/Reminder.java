/*
 * Copyright 2015 Blanyal D'Souza.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.meridian.dateout.reminder;

// Reminder class
public class Reminder {
    private int mID;
    private String mTitle;
    private String mtype;

    public String getMdetails() {
        return mdetails;
    }

    public void setMdetails(String mdetails) {
        this.mdetails = mdetails;
    }

    private String mdetails;
    private String mDate,mdate_event;
    private String mTime,mtime_event;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;

    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    private String mActive;




    public Reminder(int ID, String Title,String type,String details, String Date_event, String Time_event , String Date, String Time ,String Active){
        mID = ID;
        mTitle = Title;
        mtype=type;
        mdetails=details;
        mdate_event=Date_event;
        mtime_event=Time_event;
        mDate = Date;
        mTime = Time;

        mActive = Active;
    }

    public String getMdate_event() {
        return mdate_event;
    }

    public String getMtime_event() {
        return mtime_event;
    }

    public void setMtime_event(String mtime_event) {
        this.mtime_event = mtime_event;
    }

    public void setMdate_event(String mdate_event) {
        this.mdate_event = mdate_event;
    }

    public Reminder(String Title,String type,String details, String Date_event, String Time_event , String Date, String Time, String Active)
    {
        mTitle = Title;
        mtype=type;
        mdetails=details;
        mDate = Date;
        mTime = Time;
        mdate_event=Date_event;

        mtime_event=Time_event;
        mActive = Active;
    }


    public Reminder(){

    }

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getRepeatType() {
        return mRepeatType;
    }

    public void setRepeatType(String repeatType) {
        mRepeatType = repeatType;
    }

    public String getRepeatNo() {
        return mRepeatNo;
    }

    public void setRepeatNo(String repeatNo) {
        mRepeatNo = repeatNo;
    }

    public String getRepeat() {
        return mRepeat;
    }

    public void setRepeat(String repeat) {
        mRepeat = repeat;
    }

    public String getActive() {
        return mActive;
    }

    public void setActive(String active) {
        mActive = active;
    }
}
