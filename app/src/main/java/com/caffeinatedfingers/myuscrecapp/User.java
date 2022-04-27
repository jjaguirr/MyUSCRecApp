package com.caffeinatedfingers.myuscrecapp;

public class User {
    /**
     * @param id User ID
     * @param userName UserName
     */
    public User(String id,String userName,String uid) {
        this.userName = userName;
        this.id = id;
        this.uid=uid;
    }

    String userName;
    String id;
    String uid;

}
