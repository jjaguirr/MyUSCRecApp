package com.caffeinatedfingers.myuscrecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class BookingPage extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    RVAdapter rvAdapter;
    RecCenter recCenter;
    DAOFireBase dao;
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
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        Bundle b = getIntent().getExtras();
        String userId = b.getString("UserId");
        String userName= b.getString("UserName");
        this.user = new User (userId,userName);
        String recCenterName = b.getString("RecCenter");


        //When click on a button do add, remove calling dao.add(timeslot, user) or dao.remove() etc.
        this.recCenter = new RecCenter(recCenterName, "INFO");
        dao = new DAOFireBase();

        //Default
        date = "TODAY";
        //@TODO Buttons for today and tomorrow.

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
        dao.getTimeSlots(this.recCenter.id, this.date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot RCSnapshot) {
                String recCenter = RCSnapshot.getKey();
                for(DataSnapshot TSSnapshot: RCSnapshot.getChildren()){
                    String timeSlotID = TSSnapshot.getKey();
                    Double capacity = (Double) TSSnapshot.child("Capacity").getValue();
                    assert timeSlotID != null;
                    TimeSlot ts = rvAdapter.getTimeSlot(timeSlotID);
                    if (ts==null){
                        TimeSlot nTs = new TimeSlot(capacity, recCenter, timeSlotID);
                        rvAdapter.add(nTs);
                        nTs.setThisUserReserved(TSSnapshot.child("Registered").child(user.id).exists());
                        rvAdapter.notifyItemInserted(rvAdapter.items.indexOf(nTs));
                        nTs.usersCount = (int) TSSnapshot.child("Registered").getChildrenCount();
                    }
                    //update already existing timeslot
                    else {
                        ts.setThisUserReserved(TSSnapshot.child("Registered").child(user.id).exists());
                        int pos = rvAdapter.items.indexOf(ts);
                        rvAdapter.notifyItemChanged(pos);
                        ts.usersCount = (int) TSSnapshot.child("Registered").getChildrenCount();
                    }
                }
                /*@TODO: Highly inefficient, make it so that only each timeslot changes.
                should redefine value event listener on data change.
                 */
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}