package com.caffeinatedfingers.myuscrecapp;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TimeSlotVH extends RecyclerView.ViewHolder {
    public TextView txt_hours, txt_remaining;
    public Button btn_book;
    public TimeSlotVH(@NonNull View itemView, int viewType) {
        super(itemView);
        switch (viewType){
            case 0: {
                this.btn_book = itemView.findViewById(R.id.btn_book);
                this.txt_hours = itemView.findViewById(R.id.txt_hours);
                this.txt_remaining = itemView.findViewById(R.id.txt_remaining);
                break;
            }
            case 1: {
                this.btn_book = itemView.findViewById(R.id.btn_cancel);
                this.txt_hours = itemView.findViewById(R.id.txt_hours);
                this.txt_remaining = itemView.findViewById(R.id.txt_remaining); //or "NO SPOTS AVAL"
            }
            case 2: {
                this.btn_book = itemView.findViewById(R.id.btn_remindme);
                this.txt_hours = itemView.findViewById(R.id.txt_hours);
                this.txt_remaining = itemView.findViewById(R.id.txt_remaining);
            }
        }

    }
}
