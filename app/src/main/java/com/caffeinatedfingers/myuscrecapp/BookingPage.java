package com.caffeinatedfingers.myuscrecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
        TextView gymName = findViewById(R.id.gym_name);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        Bundle b = getIntent().getExtras();
        String userId = b.getString("UserId");
        String userName= b.getString("UserName");
        this.user = new User (userId,userName);
        String recCenterName = b.getString("RecCenter");
        gymName.setText(recCenterName);

        //When click on a button do add, remove calling dao.add(timeslot, user) or dao.remove() etc.
        this.recCenter = new RecCenter(recCenterName, "INFO");
        dao = new DAOFireBase();

        //Default
        date = "TODAY";
        Button btn_today = findViewById(R.id.btn_today);
        Button btn_tomorrow = findViewById(R.id.btn_tomorrow);
        View.OnClickListener onClickListener = view -> {
            if (date.equals("TODAY") && view.equals(btn_tomorrow)){
                view.setBackgroundResource(R.drawable.timeslot_book_background);//red pressed
                btn_today.setBackgroundResource(R.drawable.timeslot_full_background);//gray
                date = "TOMORROW";
                clearRV();
                loadData();
            }
            if (date.equals("TOMORROW") && view.equals(btn_today)){
                view.setBackgroundResource(R.drawable.timeslot_book_background);//red pressed
                btn_tomorrow.setBackgroundResource(R.drawable.timeslot_full_background);//gray
                date = "TODAY";
                clearRV();
                loadData();
            }
        };
        btn_today.setOnClickListener(onClickListener);
        btn_tomorrow.setOnClickListener(onClickListener);

        rvAdapter = new RVAdapter(this, dao, user, date);
        recyclerView.setAdapter(rvAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            loadData();
        });
        loadData();
    }

    private void clearRV(){
        rvAdapter.refreshDelete();
    }

    private void loadData(){
        swipeRefreshLayout.setRefreshing(true);
        dao.getTimeSlots(this.recCenter.id, this.date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot RCSnapshot) {
                String recCenterName = recCenter.id;
                for(DataSnapshot TSSnapshot: RCSnapshot.getChildren()){
                    String timeSlotID = TSSnapshot.getKey();
                    Long capacity = (Long) TSSnapshot.child("Capacity").getValue();
                    assert timeSlotID != null;
                    TimeSlot ts = rvAdapter.getTimeSlot(timeSlotID);
                    if (ts==null){
                        TimeSlot nTs = new TimeSlot(capacity, recCenterName, timeSlotID, date);
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