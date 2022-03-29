package com.caffeinatedfingers.myuscrecapp;

import java.io.Serializable;

public class Reservation implements Serializable {
    public String studentID;
    public String time;
    public String location;
    public String id;
    public Long cap;
    public String date;

    public Reservation(){
        //no argument constructor
    }
    public Reservation(String studentID, String time, String recCenter, Long cap, String date) {
        this.studentID = studentID;
        this.time = time;
        this.location = recCenter;
        this.cap = cap;
        this.id = studentID+recCenter+time;
        this.date = date;
    }
}
