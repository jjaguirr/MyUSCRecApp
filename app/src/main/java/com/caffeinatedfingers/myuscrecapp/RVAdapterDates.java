package com.caffeinatedfingers.myuscrecapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapterDates extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    ArrayList<String> items;
    public String pressedDate;

    public void setClickListener(View.OnClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    View.OnClickListener mClickListener;
    public RVAdapterDates (Context context){
        this.items = new ArrayList<>();
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_button_date, parent, false);
        return new DateButtonVH(view);
    }

    public void setPressedDate(String pressedDate) {
        this.pressedDate = pressedDate;
    }

    public void addDate(String date){
        items.add(date);
        if(items.size() == 1) setPressedDate(date);
        notifyItemInserted(items.indexOf(date));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DateButtonVH buttonVH = (DateButtonVH) holder;
        String date = items.get(position);
        buttonVH.btn_date.setText(date);
        Drawable background;
        if(pressedDate.equals(items.get(position))) {
            background = AppCompatResources.getDrawable(context, R.drawable.timeslot_book_background);
        }
        else{
            background = AppCompatResources.getDrawable(context, R.drawable.timeslot_full_background);
        }
        buttonVH.btn_date.setBackground(background);

        buttonVH.btn_date.setOnClickListener(mClickListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class DateButtonVH extends RecyclerView.ViewHolder{
        public Button btn_date;
        public int viewType;
        public DateButtonVH(@NonNull View itemView) {
            super(itemView);
             this.btn_date = itemView.findViewById(R.id.btn_date);
        }
    }
}
