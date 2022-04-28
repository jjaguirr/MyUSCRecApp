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

public class RVAdapterReservation extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    DAOFireBase dao;
    User user;
    ArrayList<Reservation> items = new ArrayList<>();
    String date;
    public RVAdapterReservation(Context context, DAOFireBase dao, User user){
        this.context = context;
        this.dao = dao;
        this.user = user;
    }
    //@TODO specify item for efficiency purposes
    public void add(Reservation rs) {
        if (this.items.contains(rs)) return;
        this.items.add(rs);
    }

    public void delete(Reservation ts) {
        this.items.remove(ts);
    }

    public void refreshDelete(){
        items.clear();
        this.notifyDataSetChanged();
    }
    public Reservation getReservation(@NonNull String id){
        for (Reservation rs: items){
            if (rs.id.equals(id)) return rs;
        }
        return null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case 0: view = LayoutInflater.from(context).inflate(R.layout.layout_reservation_upcoming, parent, false);
            break;
            case 1: view = LayoutInflater.from(context).inflate(R.layout.layout_reservation_previous, parent, false);
            break;
            default: throw new IllegalStateException("Unexpected value: " + viewType);
        }
        return new ReservationVH(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ReservationVH vh = (ReservationVH) holder;
        Reservation reservation = items.get(position);
        if (getItemViewType(position)==0){
            vh.btn.setOnClickListener(view -> {
                dao.removeReservation(reservation, context);
                items.remove(reservation);
                notifyItemRemoved(position);
            });
        }
        String dateAndTime = reservation.date+" "+reservation.time;
        vh.txt_date.setText(dateAndTime);
        String location = reservation.location;
        vh.txt_location.setText(location);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
        //if
    }

    public class ReservationVH extends RecyclerView.ViewHolder {
        public TextView txt_date, txt_location;
        public Button btn;
        public ReservationVH(@NonNull View itemView, int viewType) {
            super(itemView);
            if(viewType==0) this.btn=itemView.findViewById(R.id.btn_cancel);
            this.txt_date = itemView.findViewById(R.id.txt_date);
            this.txt_location = itemView.findViewById(R.id.txt_place);
        }

    }
}
