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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class ReminderDatabase extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ReminderDatabase";

    // Table name
    private static final String TABLE_REMINDERS = "ReminderTable";

    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TYPE = "type";
    private static final String KEY_DATE = "rel_date_remind";
    private static final String KEY_TIME = "rel_time_remind";
    private static final String KEY_DATE_EVENT = "rel_date_EVENT";
    private static final String KEY_TIME_EVENT = "rel_time_EVENT";
    private static final String KEY_details = "details";
    private static final String KEY_REPEAT = "repeat";
    private static final String KEY_REPEAT_NO = "repeat_no";
    private static final String KEY_REPEAT_TYPE = "repeat_type";
    private static final String KEY_ACTIVE = "active";

    public ReminderDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REMINDERS_TABLE = "CREATE TABLE " + TABLE_REMINDERS +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_TYPE + " TEXT,"
                + KEY_details+ " TEXT,"
                + KEY_DATE_EVENT + " TEXT,"
                + KEY_TIME_EVENT + " INTEGER,"
                + KEY_DATE + " TEXT,"
                + KEY_TIME + " INTEGER,"
                + KEY_ACTIVE + " BOOLEAN" + ")";
        db.execSQL(CREATE_REMINDERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        if (oldVersion >= newVersion)
            return;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);

        // Create tables again
        onCreate(db);
    }

    // Adding new Reminder
    public int addReminder(Reminder reminder){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE , reminder.getTitle());
        values.put(KEY_TYPE , reminder.getMtype());
        values.put(KEY_details , reminder.getMdetails());

        values.put(KEY_DATE_EVENT , reminder.getMdate_event());
        values.put(KEY_TIME_EVENT , reminder.getMtime_event());
        values.put(KEY_DATE , reminder.getDate());
        values.put(KEY_TIME , reminder.getTime());

        values.put(KEY_ACTIVE, reminder.getActive());

        // Inserting Row
        long ID = db.insert(TABLE_REMINDERS, null, values);
        db.close();
        return (int) ID;
    }

    // Getting single Reminder
    public Reminder getReminder(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REMINDERS, new String[]
                        {
                                KEY_ID,
                                KEY_TITLE,
                                KEY_TYPE,
                                KEY_details,

                                KEY_DATE_EVENT,
                                KEY_TIME_EVENT,
                                KEY_DATE,
                                KEY_TIME,

                                KEY_ACTIVE
                        }, KEY_ID + "=?",

                new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Reminder reminder = new Reminder(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),cursor.getString(7),cursor.getString(8));

        return reminder;
    }

    // Getting all Reminders
    public List<Reminder> getAllReminders(){
        List<Reminder> reminderList = new ArrayList<>();

        // Select all Query
        String selectQuery = "SELECT * FROM " + TABLE_REMINDERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if(cursor.moveToFirst()){
            do{
                Reminder reminder = new Reminder();
                reminder.setID(Integer.parseInt(cursor.getString(0)));
                reminder.setTitle(cursor.getString(1));
                reminder.setMtype(cursor.getString(2));
                reminder.setMdetails(cursor.getString(3));
                reminder.setMdate_event(cursor.getString(4));
                reminder.setMtime_event(cursor.getString(5));
                reminder.setDate(cursor.getString(6));
                reminder.setTime(cursor.getString(7));

                reminder.setActive(cursor.getString(8));

                // Adding Reminders to list
                reminderList.add(reminder);
            } while (cursor.moveToNext());
        }
        return reminderList;
    }

    // Getting Reminders Count
    public int getRemindersCount(){
        String countQuery = "SELECT * FROM " + TABLE_REMINDERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);
        cursor.close();

        return cursor.getCount();
    }
    public String[] getEventTypes(){
        String[] event_types;

// Select all Query
        String selectQuery = "SELECT "+KEY_TYPE+" FROM " + TABLE_REMINDERS;
        System.out.println("selectQuery : "+selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

// Looping through all rows and adding to list
        event_types=new String[cursor.getCount()];
        if(cursor.moveToFirst()){
            do{

                event_types[cursor.getPosition()]=cursor.getString(0);


            } while (cursor.moveToNext());
        }
        return event_types;
    }
    // Updating single Reminder
    public int updateReminder(Reminder reminder){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE , reminder.getTitle());
        values.put(KEY_TYPE , reminder.getMtype());
        values.put(KEY_details , reminder.getMdetails());
        values.put(KEY_DATE_EVENT , reminder.getMdate_event());
        values.put(KEY_TIME_EVENT , reminder.getMtime_event());

        values.put(KEY_DATE , reminder.getDate());
        values.put(KEY_TIME , reminder.getTime());

        values.put(KEY_ACTIVE, reminder.getActive());

        // Updating row
        return db.update(TABLE_REMINDERS, values, KEY_ID + "=?",
                new String[]{String.valueOf(reminder.getID())});
    }

    // Deleting single Reminder
    public void deleteReminder(Reminder reminder){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMINDERS, KEY_ID + "=?",
                new String[]{String.valueOf(reminder.getID())});
        db.close();
    }
    public int changeStatus(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.beginTransaction();
        int i=0;
        try{
            String update_query="UPDATE "+TABLE_REMINDERS+" SET "+KEY_ACTIVE+"='false' WHERE "+KEY_ID+"='"+id+"'";
            System.out.println("update query : "+update_query);
            db.execSQL(update_query);
            db.setTransactionSuccessful();
            i=1;

        }catch (Exception e){
            e.printStackTrace();
            i=0;
        }finally {
            db.endTransaction();
            db.close();
        }
        return i;
    }
}
