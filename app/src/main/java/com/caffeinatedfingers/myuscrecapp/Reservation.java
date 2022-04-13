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

    public Reservation(User user, TimeSlot timeSlot) {
        this.studentID = user.id;
        this.time = timeSlot.time;
        this.location = timeSlot.recCenter;
        this.cap = timeSlot.capacity;
        this.id = studentID+timeSlot.recCenter+time;
        this.date = timeSlot.date;
    }
}
