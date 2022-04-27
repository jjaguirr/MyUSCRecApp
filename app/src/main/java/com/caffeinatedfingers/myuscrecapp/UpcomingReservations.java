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

public class UpcomingReservations extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout, swipeRPrevious;
    RecyclerView RVUpcoming, RVPrevious;
    RVAdapterReservation rvAdapterUpcoming, rvAdapterPrevious;
    DAOFireBase dao;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_upcoming_page);

        swipeRefreshLayout = findViewById(R.id.swipe);
        RVUpcoming = findViewById(R.id.rv);
        RVUpcoming.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        RVUpcoming.setLayoutManager(manager);


        swipeRPrevious = findViewById(R.id.swipePrevious);
        RVPrevious = findViewById(R.id.rvPrevious);
        RVPrevious.setHasFixedSize(true);
        LinearLayoutManager managerPrevious = new LinearLayoutManager(this);
        RVPrevious.setLayoutManager(managerPrevious);

        Bundle b = getIntent().getExtras();
        String userId = b.getString("UserId");
        String userName= b.getString("UserName");
        String uid=b.getString("UID");
        this.user = new User (userId,userName,uid);

        dao = new DAOFireBase();

        rvAdapterUpcoming = new RVAdapterReservation(this, dao, user);
        rvAdapterPrevious = new RVAdapterReservation(this, dao, user);

        RVUpcoming.setAdapter(rvAdapterUpcoming);
        RVPrevious.setAdapter(rvAdapterPrevious);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            loadUpcoming();
        });
        swipeRPrevious.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            loadPrevious();
        });
        loadUpcoming();
        loadPrevious();
    }

    private void clearRVUpcoming(){
        rvAdapterUpcoming.refreshDelete();
    }
    private void clearRVPrevious(){
        rvAdapterPrevious.refreshDelete();
    }

    private void loadUpcoming(){
        swipeRefreshLayout.setRefreshing(true);
        dao.getReservationsQuery(this.user.id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userRSS) {
                if (userRSS.getValue() == null){
                    rvAdapterUpcoming.refreshDelete();
                }
                for (DataSnapshot reservationSS: userRSS.getChildren()){
                    Reservation reservation= reservationSS.getValue(Reservation.class);
                    assert reservation != null;
                    Reservation reservationInAdapter = rvAdapterUpcoming.getReservation(reservation.id);
                    if (reservationInAdapter==null){
                        rvAdapterUpcoming.add(reservation);
                        rvAdapterUpcoming.notifyItemInserted(rvAdapterUpcoming.items.indexOf(reservation));
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

    private void loadPrevious(){
        swipeRPrevious.setRefreshing(true);
        dao.getPreviousReservationQuery(this.user.id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userRSS) {
                if (userRSS.getValue() == null){
                    rvAdapterPrevious.refreshDelete();
                }
                //For all reservations of this user.
                for (DataSnapshot reservationSS: userRSS.getChildren()){
                    Reservation reservationFromDB= reservationSS.getValue(Reservation.class);
                    assert reservationFromDB != null;
                    //if not in the RV add it
                    if (rvAdapterPrevious.getReservation(reservationFromDB.id)==null){
                        rvAdapterPrevious.add(reservationFromDB);
                        rvAdapterPrevious.notifyItemInserted(rvAdapterPrevious.items.indexOf(reservationFromDB));
                    }
                }
                swipeRPrevious.setRefreshing(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRPrevious.setRefreshing(false);
            }
        });
    }
}