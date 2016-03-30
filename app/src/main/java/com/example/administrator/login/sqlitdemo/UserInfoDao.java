package com.example.administrator.login.sqlitdemo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.login.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-3-23.
 */
public class UserInfoDao {
    PersonSQLiteOpenHelper mOpenHeple;
    public UserInfoDao(Context context){
        mOpenHeple = new PersonSQLiteOpenHelper(context);
    }

    public void insert(UserInfo userInfo){
        //获得可写的数据库对象
        SQLiteDatabase db = mOpenHeple.getWritableDatabase();
        if (db.isOpen()){
            db.execSQL("insert into userinfo(username,pswd) values(?,?)",
                    new Object[]{userInfo.getUsernaem(),userInfo.getPswd()});
            db.close();
        }
    }

    public void delete(int id){
        SQLiteDatabase db = mOpenHeple.getWritableDatabase();
        if (db.isOpen()){
            db.execSQL("delete from userinfo where _id = ?;", new Integer[]{id});
            db.close();
        }
    }

    public void updata(int id,String username){
        SQLiteDatabase db = mOpenHeple.getWritableDatabase();
        if (db.isOpen()){
            db.execSQL("updata userinfo set username = ? where _id = ?;", new Object[]{username,id});
            db.close();
        }
    }

    public List<UserInfo> queryAll(){
        SQLiteDatabase db = mOpenHeple.getReadableDatabase();
        if (db.isOpen()){
            Cursor cursor = db.rawQuery("select * from userinfo;", null);
            if (cursor != null && cursor.getCount() > 0){
                List<UserInfo> userInfoList = new ArrayList<>();
                int id;
                String username;
                String pswd;
                while (cursor.moveToNext()){
                    id = cursor.getInt(0);
                    username = cursor.getString(1);
                    pswd = cursor.getString(2);
                    userInfoList.add(new UserInfo(id,username,pswd));
                }
                db.close();
                return userInfoList;
            }
            db.close();
        }
        return null;
    }

    public String queryPswdByUsername(String username){
        SQLiteDatabase db = mOpenHeple.getReadableDatabase();
        if (db.isOpen()){
           Cursor cursor = db.rawQuery("select pswd from userinfo where username = ?;",
                   new String[]{username});
            if (cursor != null && cursor.moveToFirst()) {
                String pswd = cursor.getString(0);
                db.close();
                return pswd;
            }
        }
        return null;
    }
}
