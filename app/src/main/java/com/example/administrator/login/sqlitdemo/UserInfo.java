package com.example.administrator.login.sqlitdemo;

/**
 * Created by Administrator on 2016-3-23.
 */
public class UserInfo {

    private int id;
    private String usernaem;
    private String  pswd;

    public UserInfo(){}

    public UserInfo(int id, String usernaem, String pswd){
        this.id = id;
        this.usernaem = usernaem;
        this.pswd = pswd;
        return;
    }

    public int getId() {
        return id;
    }

    public String getUsernaem() {
        return usernaem;
    }

    public String getPswd() {
        return pswd;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsernaem(String usernaem) {
        this.usernaem = usernaem;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", usernaem='" + usernaem + '\'' +
                ", pswd=" + pswd +
                '}';
    }
}
