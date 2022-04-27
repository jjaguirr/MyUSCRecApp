package com.caffeinatedfingers.myuscrecapp;

import java.io.Serializable;

public class User implements Serializable {
    public String uid;
    public String userName;
    public String id;

    /**
     * @param id User ID
     * @param userName UserName
     */
    public User(String id,String userName,String uid) {
        this.userName = userName;
        this.id = id;
        this.uid=uid;
    }

    public User(){
        //No argument Constructor, required for Serialization
    }

}
