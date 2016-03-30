package com.example.administrator.login.sqlitdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016-3-23.
 */
public class PersonSQLiteOpenHelper extends SQLiteOpenHelper {
    public PersonSQLiteOpenHelper(Context context) {
        super(context, "login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table userinfo( id integer primary key, username verchar(20),pswd verchar(20));";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
