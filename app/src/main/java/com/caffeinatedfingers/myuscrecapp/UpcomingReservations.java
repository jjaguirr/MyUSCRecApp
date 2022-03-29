package com.caffeinatedfingers.myuscrecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    String date;
    boolean isLoading=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_page);
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
        dao.getReservations(this.user.id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userRSS) {
                if (userRSS.getValue() == null){
                    rvAdapter.refreshDelete();
                }
                for (DataSnapshot reservationSS: userRSS.getChildren()){
                    Reservation reservation= reservationSS.getValue(Reservation.class);
                    assert reservation != null;
                    String timeSlotID = reservation.time;
                    TimeSlot ts = rvAdapter.getTimeSlot(timeSlotID);
                    if (ts==null){
                        TimeSlot nTs = new TimeSlot(reservation.cap, reservation.location, timeSlotID, date);
                        dao.getTimeSlotQuery(nTs).get().addOnSuccessListener(dataSnapshot -> {
                            nTs.usersCount = (int) dataSnapshot.getChildrenCount();
                            nTs.setThisUserReserved(dataSnapshot.child(user.id).exists());
                            rvAdapter.add(nTs);
                            rvAdapter.notifyItemInserted(rvAdapter.items.indexOf(nTs));
                        });

                    }
                    //update already existing timeslot
                    else {
                        int pos = rvAdapter.items.indexOf(ts);
                        if(!ts.thisUserReserved) {
                            rvAdapter.delete(ts);
                            rvAdapter.notifyItemRemoved(pos);
                        }
                        else{
                            rvAdapter.notifyItemChanged(pos);
                            dao.getTimeSlotQuery(ts).get().addOnSuccessListener(dataSnapshot -> {
                                ts.usersCount = (int) dataSnapshot.getChildrenCount();
                                ts.setThisUserReserved(dataSnapshot.child(user.id).exists());
                            });
                        }
                    }
                }
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

}