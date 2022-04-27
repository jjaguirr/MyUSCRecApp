package com.caffeinatedfingers.myuscrecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class BookingPage extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView, rvDates;
    RVAdapter rvAdapter;
    RVAdapterDates rvAdapterDates;
    RecCenter recCenter;
    DAOFireBase dao;
    ArrayList<String> dates;
    String date;
    User user;
    boolean isLoading=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page);
        swipeRefreshLayout = findViewById(R.id.swipe);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        TextView gymName = findViewById(R.id.gym_name);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        Bundle b = getIntent().getExtras();
        String userId = b.getString("UserId");
        String userName= b.getString("UserName");
        String uid=b.getString("UID");
        this.user = new User (userId,userName,uid);
        String recCenterName = b.getString("RecCenter");
        gymName.setText(recCenterName);

        //When click on a button do add, remove calling dao.add(timeslot, user) or dao.remove() etc.
        this.recCenter = new RecCenter(recCenterName, "INFO");
        dao = new DAOFireBase();

        rvDates = findViewById(R.id.recycler_view_button_dates);
        LinearLayoutManager datesLLM = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvDates.setLayoutManager(datesLLM);
        rvAdapterDates = new RVAdapterDates(this);
        rvDates.setAdapter(rvAdapterDates);
        rvAdapterDates.setClickListener(view -> {
            Button clickedB = (Button)view;
            String newDate = clickedB.getText().toString();
            rvAdapterDates.notifyItemChanged(rvAdapterDates.items.indexOf(rvAdapterDates.pressedDate));
            rvAdapterDates.notifyItemChanged(rvAdapterDates.items.indexOf(newDate));
            rvAdapterDates.pressedDate = newDate;
            date = newDate;
            clearRV();
            loadTimeSlots();
        });
        rvAdapter = new RVAdapter(this, dao, user);
        recyclerView.setAdapter(rvAdapter);

        loadDates();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            loadTimeSlots();
        });
    }

    private void clearRV(){
        rvAdapter.refreshDelete();
    }

    public void loadTimeSlots(){
        swipeRefreshLayout.setRefreshing(true);
        dao.getTimeSlotsQuery(this.recCenter.id, rvAdapterDates.pressedDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot RCSnapshot) {
                String recCenterName = recCenter.id;
                for(DataSnapshot TSSnapshot: RCSnapshot.getChildren()){
                    TimeSlot example = TSSnapshot.getValue(TimeSlot.class);
                    String timeSlotID = TSSnapshot.getKey();
                    assert timeSlotID != null;
                    LocalTime timeSlotEndingTime = LocalTime.parse(timeSlotID.split("-")[1], DateTimeFormatter.ofPattern("hha"));
                    LocalDateTime timeSlotDateTime = LocalDateTime.of(LocalDate.parse(rvAdapterDates.pressedDate, DateTimeFormatter.ofPattern("MM-dd-yyyy")),
                            timeSlotEndingTime);
                    if(timeSlotDateTime.isBefore(LocalDateTime.now())) continue;
                    Long capacity = (Long) TSSnapshot.child("capacity").getValue();
                    TimeSlot ts = rvAdapter.getTimeSlot(timeSlotID);
                    if (ts==null){
                        TimeSlot nTs = new TimeSlot(capacity, recCenterName, timeSlotID, date);
                        rvAdapter.add(nTs);
                        nTs.setThisUserReserved(TSSnapshot.child("Registered").child(user.id).exists());
                        nTs.setThisUserInWaitlist(TSSnapshot.child("Waitlist").child(user.id).exists());
                        rvAdapter.notifyItemInserted(rvAdapter.items.indexOf(nTs));
                        nTs.usersCount = (int) TSSnapshot.child("Registered").getChildrenCount();
                    }
                    //update already existing timeslot
                    else {
                        ts.setThisUserReserved(TSSnapshot.child("Registered").child(user.id).exists());
                        ts.setThisUserInWaitlist(TSSnapshot.child("Waitlist").child(user.id).exists());
                        int pos = rvAdapter.items.indexOf(ts);
                        rvAdapter.notifyItemChanged(pos);
                        ts.usersCount = (int) TSSnapshot.child("Registered").getChildrenCount();
                    }
                }
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public void loadDates(){
        dao.getDatesQuery(recCenter.id).get().addOnSuccessListener(snapshot -> {
            if(!snapshot.hasChildren()) rvAdapterDates.addDate("NODATE"); //or smth else
            for(DataSnapshot dateSS: snapshot.getChildren()){
                String date = dateSS.getKey();
                if (LocalDate.now().isAfter(LocalDate.parse(date,DateTimeFormatter.ofPattern("MM-dd-yyyy"))))
                    continue;
                if (rvAdapterDates.items.contains(date)) continue;
                rvAdapterDates.addDate(date);
            }
            date = rvAdapterDates.pressedDate;
            loadTimeSlots();
        });

    }
}