package com.caffeinatedfingers.myuscrecapp;

import java.io.Serializable;

public class Reservation implements Serializable {
    String studentID;
    String time;
    String location;
    String id;
    Double cap;
    String date;

    public Reservation(String studentID, String time, String recCenter, Double cap, String date) {
        this.studentID = studentID;
        this.time = time;
        this.location = recCenter;
        this.cap = cap;
        this.id = studentID+recCenter+time;
        this.date = date;
    }
}
