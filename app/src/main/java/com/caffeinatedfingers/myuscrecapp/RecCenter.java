package com.caffeinatedfingers.myuscrecapp;

import java.util.List;

//Construct from database
public class RecCenter {
    public RecCenter(String id, String info) {
        this.id = id;
        this.info = info;
    }

    String id;
    List<String> times;
    String hours; //maybe use a class for hours and min, like time.
    String info;

}
