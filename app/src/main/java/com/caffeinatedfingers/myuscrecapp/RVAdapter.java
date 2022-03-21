package com.caffeinatedfingers.myuscrecapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.TooManyListenersException;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    ArrayList<TimeSlot> items = new ArrayList<>();
    public RVAdapter(Context context){
        this.context = context;
    }

    public void setItems(ArrayList<TimeSlot> items) {
        this.items.addAll(items);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_time_slot, parent, false);
        switch (viewType){
            case 1: view = LayoutInflater.from(context).inflate(R.layout.layout_time_slot_full, parent, false);
            break;
            case 2: view = LayoutInflater.from(context).inflate(R.layout.layout_time_slot_reserved, parent, false);
            break;
        }
        return new TimeSlotVH(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TimeSlotVH vh = (TimeSlotVH) holder;
        TimeSlot timeSlot = items.get(position);
        vh.txt_hours.setText(timeSlot.time);
        vh.txt_remaining.setText(String.valueOf(timeSlot.getRemaining()));
        //button??
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        TimeSlot ts = items.get(position);
        if (!ts.isAvailable()) return 1;
        else if (ts.isReserved()) return 2;
        else return (0);
    }
}
