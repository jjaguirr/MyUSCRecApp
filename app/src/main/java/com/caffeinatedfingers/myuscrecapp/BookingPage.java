package com.caffeinatedfingers.myuscrecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookingPage extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    RVAdapter rvAdapter;
    RecCenter recCenter;
    User user = new User ("0001", "JOJO");
    DAORecCenter dao;
    String key = null;
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
        String recCenterName = b.getString("RecCenter");



        //When click on a button do add, remove calling dao.add(timeslot, user) or dao.remove() etc.
        this.recCenter = new RecCenter(recCenterName, "INFO");
        dao = new DAORecCenter(this.recCenter);


        rvAdapter = new RVAdapter(this, dao, user);
        recyclerView.setAdapter(rvAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert llm != null;
                int totalItem = llm.getItemCount();
                int lastVisible = llm.findLastVisibleItemPosition();
                if (totalItem< lastVisible+3){
                    if (!isLoading) {
                        isLoading = true;
                        loadData();
                    }
                }
            }
        });
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
        dao.get(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    String timeSlotID = data.getKey();
                    String recCenter = snapshot.getKey();
                    //get capacity, hardcoded for now
                    //create ts if not found
                    assert timeSlotID != null;
                    TimeSlot ts = rvAdapter.getTimeSlot(timeSlotID);
                    if (ts==null){
                        TimeSlot nTs = new TimeSlot(3, recCenter, timeSlotID);
                        rvAdapter.add(nTs);
                        nTs.setThisUserReserved(data.child(user.id).exists());
                        rvAdapter.notifyItemInserted(rvAdapter.items.indexOf(nTs));
                        nTs.usersCount = (int) data.getChildrenCount();
                    }
                    //update already existing timeslot
                    else {
                        ts.setThisUserReserved(data.child(user.id).exists());
                        int pos = rvAdapter.items.indexOf(ts);
                        rvAdapter.notifyItemChanged(pos);
                        ts.usersCount = (int) data.getChildrenCount();
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