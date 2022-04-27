package com.caffeinatedfingers.myuscrecapp;

import java.io.Serializable;

public class User implements Serializable {
    public String userName;
    public String id;
    public String uid;

    /**
     * @param id User ID
     * @param userName UserName
     */
    public User(String id,String userName,String uid) {
        this.userName = userName;
        this.id = id;
        this.uid=uid;
    }

  
}
