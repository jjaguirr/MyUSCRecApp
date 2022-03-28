package com.caffeinatedfingers.myuscrecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpcomingReservations extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    RVAdapter rvAdapter;
    DAOFireBase dao;
    User user;
    boolean isLoading=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page);
        swipeRefreshLayout = findViewById(R.id.swipe);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        Bundle b = getIntent().getExtras();
        String userId = b.getString("UserId");
        String userName= b.getString("UserName");
        this.user = new User (userId,userName);



        //When click on a button do add, remove calling dao.add(timeslot, user) or dao.remove() etc.
        dao = new DAOFireBase();


        rvAdapter = new RVAdapter(this, dao, user);
        recyclerView.setAdapter(rvAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                loadData();
            }
        });
        loadData();
    }

    private void loadData(){
        swipeRefreshLayout.setRefreshing(true);
        dao.getReservations(this.user.id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userRSS) {
                for (DataSnapshot reservationSS: userRSS.getChildren()){
                    Reservation reservation = (Reservation) reservationSS.getValue();
                    assert reservation != null;
                    String timeSlotID = reservation.time;
                    TimeSlot ts = rvAdapter.getTimeSlot(timeSlotID);
                    if (ts==null){
                        TimeSlot nTs = new TimeSlot(reservation.cap, reservation.location, timeSlotID);
                        rvAdapter.add(nTs);
                        rvAdapter.notifyItemInserted(rvAdapter.items.indexOf(nTs));
                        nTs.usersCount= dao.getTimeSlotCount(nTs);
                    }
                    //update already existing timeslot
                    else {
                        int pos = rvAdapter.items.indexOf(ts);
                        rvAdapter.notifyItemChanged(pos);
                        ts.usersCount = dao.getTimeSlotCount(ts);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}