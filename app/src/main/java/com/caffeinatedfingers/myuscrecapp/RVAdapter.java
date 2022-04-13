package com.caffeinatedfingers.myuscrecapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    DAOFireBase dao;
    User user;
    ArrayList<TimeSlot> items = new ArrayList<>();
    String date;
    public RVAdapter(Context context, DAOFireBase dao, User user, String date){
        this.context = context;
        this.dao = dao;
        this.user = user;
        this.date = date;
    }
    //@TODO specify item for efficiency purposes
    public void add(TimeSlot ts) {
        this.items.add(ts);
    }

    public void delete(TimeSlot ts) {
        this.items.remove(ts);
    }

    public void refreshDelete(){
        items.clear();
        this.notifyDataSetChanged();
    }
    public TimeSlot getTimeSlot(@NonNull String id){
        for (TimeSlot ts: items){
            if (ts.id.equals(id)) return ts;
        }
        return null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType){
            case 0: view = LayoutInflater.from(context).inflate(R.layout.layout_time_slot, parent, false);
            break;
            case 1: view = LayoutInflater.from(context).inflate(R.layout.layout_time_slot_reserved, parent, false);
            break;
            case 2: view = LayoutInflater.from(context).inflate(R.layout.layout_time_slot_full, parent, false);
            break;
            case 4: view = LayoutInflater.from(context).inflate(R.layout.layout_time_slot_full_unremind_me, parent, false);
            break;
            case 5: view = LayoutInflater.from(context).inflate(R.layout.layout_reservation_previous, parent, false);
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
        return new TimeSlotVH(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TimeSlotVH vh = (TimeSlotVH) holder;
        TimeSlot timeSlot = items.get(position);
        vh.txt_hours.setText(timeSlot.time);
        String text = String.valueOf(timeSlot.getRemaining()) + " SPOTS LEFT";
        vh.txt_remaining.setText(text);
        vh.btn.setOnClickListener(view -> {
            switch (timeSlot.getViewType()){
                case 0: dao.addUser(timeSlot,user,context);
                    break;
                case 1: dao.removeUser(timeSlot,user,context);
                    break;
                case 2: dao.remindUser(timeSlot,user,context);
                    break;
                case 4: dao.unRemindUser(timeSlot,user,context);
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    public class TimeSlotVH extends RecyclerView.ViewHolder {
        public TextView txt_hours, txt_remaining;
        public Button btn;
        public TimeSlotVH(@NonNull View itemView, int viewType) {
            super(itemView);
            switch (viewType){
                case 0: {
                    this.btn = itemView.findViewById(R.id.btn_book);
                    this.txt_hours = itemView.findViewById(R.id.txt_hours);
                    this.txt_remaining = itemView.findViewById(R.id.txt_remaining);
                    break;
                }
                case 1: {
                    this.btn = itemView.findViewById(R.id.btn_cancel);
                    this.txt_hours = itemView.findViewById(R.id.txt_hours);
                    this.txt_remaining = itemView.findViewById(R.id.txt_remaining); //or "NO SPOTS AVAL"
                    break;
                }
                case 2: {
                    this.btn = itemView.findViewById(R.id.btn_remindme);
                    this.txt_hours = itemView.findViewById(R.id.txt_hours);
                    this.txt_remaining = itemView.findViewById(R.id.txt_remaining);
                    break;
                }
                case 4:{
                    this.btn = itemView.findViewById(R.id.btn_unremindme);
                    this.txt_hours = itemView.findViewById(R.id.txt_hours);
                    this.txt_remaining = itemView.findViewById(R.id.txt_remaining);
                }
            }

        }

    }
}
