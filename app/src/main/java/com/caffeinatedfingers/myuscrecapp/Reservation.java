package com.caffeinatedfingers.myuscrecapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Reservation implements Serializable {
    public String studentID;
    public String time;
    public String location;
    public String id;
    public Long cap;
    public User user;
    public TimeSlot timeSlot;
    public String date;
    public String startingTime, endingTime;

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
        String[] timeFormatted = this.time.split("-");
        this.startingTime = timeFormatted[0];
        this.endingTime = timeFormatted[1];
    }

}
