package com.company;

public class DatabaseDetail {

    private String url;
    private String user;
    private String password;

    public DatabaseDetail(String url, String user, String password){
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public String toString(){
        return "DeviceJavaObjectClass :::> url: "+ url + "user" + user + "password";
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}