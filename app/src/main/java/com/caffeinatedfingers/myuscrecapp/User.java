package com.caffeinatedfingers.myuscrecapp;

import java.io.Serializable;

public class User implements Serializable {
    public String userName;
    public String id;

    /**
     * @param id User ID
     * @param userName UserName
     */
    public User(String id,String userName) {
        this.userName = userName;
        this.id = id;
    }

    public User(){
        //No argument Constructor, required for Serialization
    }

}
