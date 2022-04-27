package com.caffeinatedfingers.myuscrecapp;

import java.io.Serializable;

public class Reservation implements Serializable {
    public String studentID;
    public String time;
    public String location;
    public String id;
    public Long cap;
    public User user;
    public TimeSlot timeSlot;
    public String date;

    public Reservation(){
        //no argument constructor
    }

    public Reservation(User user, TimeSlot timeSlot) {
        this.user= user;
        this.timeSlot=timeSlot;
        this.studentID = user.id;
        this.time = timeSlot.time;
        this.location = timeSlot.recCenter;
        this.cap = timeSlot.capacity;
        this.date = timeSlot.date;
        this.id = studentID+timeSlot.recCenter+time+date;
    }
}
