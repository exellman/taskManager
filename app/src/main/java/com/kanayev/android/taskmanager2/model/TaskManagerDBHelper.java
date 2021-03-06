package com.kanayev.android.taskmanager2.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskManagerDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ToDoDBHelper.db";
    private static final String CONTACTS_TABLE_NAME = "todo";

    public TaskManagerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE todo(id INTEGER PRIMARY KEY, task TEXT, dateStr INTEGER, isSolved NUMERIC, description TEXT, interval TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS todo");
        onCreate(db);
    }

    private long getDate(String day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm", Locale.getDefault());
        Date date = new Date();
        try {
            date = dateFormat.parse(day);
        } catch (ParseException ignored) {
        }
        return date.getTime();
    }

    public void insertContact(String task, String dateStr, String isSolved, String description, String interval) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        contentValues.put("dateStr", getDate(dateStr));
        contentValues.put("isSolved", isSolved);
        contentValues.put("description", description);
        contentValues.put("interval", interval);
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
    }

    public void updateContact(String id, String task, String dateStr, String isSolved, String description, String interval) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        contentValues.put("dateStr", getDate(dateStr));
        contentValues.put("isSolved", isSolved);
        contentValues.put("description", description);
        contentValues.put("interval", interval);
        db.update(CONTACTS_TABLE_NAME, contentValues, "id = ? ", new String[]{id});
    }

    public void removeTask(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + CONTACTS_TABLE_NAME + " where id = '" + id + "'");
        db.close();
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " order by id desc", null);
        return res;

    }

    public Cursor getDataSpecific(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " WHERE id = '" + id + "' order by id desc", null);
        return res;

    }

    public Cursor getDataBefore() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME +
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) < date('now', 'localtime') order by id desc", null);
        return res;

    }

    public Cursor getDataToday() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME +
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) = date('now', 'localtime') order by id desc", null);
        return res;
    }

    public Cursor getDataTomorrow() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME +
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) = date('now', '+1 day', 'localtime')  order by id desc", null);
        return res;

    }

    public Cursor getDataUpcoming() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME +
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) > date('now', '+1 day', 'localtime') order by id desc", null);
        return res;

    }
}

