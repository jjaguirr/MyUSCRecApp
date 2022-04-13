package com.caffeinatedfingers.myuscrecapp;

public class User {
    /**
     * @param id User ID
     * @param userName UserName
     */
    public User(String id,String userName) {
        this.userName = userName;
        this.id = id;
    }

    String userName;
    String id;

}
