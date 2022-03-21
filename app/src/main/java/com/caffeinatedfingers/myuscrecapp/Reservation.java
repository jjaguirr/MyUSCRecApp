package com.caffeinatedfingers.myuscrecapp;

public class Reservation {
    public Reservation(User student, String time, String location) {
        this.student = student;
        this.time = time;
        this.location = location;
    }

    User student;
    String time;
    String location;
    //waitlisted
}
