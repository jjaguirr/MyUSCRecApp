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

import java.util.ArrayList;

public class BookingPage extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    RVAdapter rvAdapter;
    RecCenter recCenter;
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

        rvAdapter = new RVAdapter(this);
        recyclerView.setAdapter(rvAdapter);

        Bundle b = getIntent().getExtras();
        String recCenterName = b.getString("RecCenter");

        //When click on a button do add, remove calling dao.add(timeslot, user) or dao.remove() etc.
        this.recCenter = new RecCenter(recCenterName, "INFO");
        dao = new DAORecCenter(this.recCenter);

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

        loadData();
    }
    private void loadData(){
        swipeRefreshLayout.setRefreshing(true);
        dao.get(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<TimeSlot> temp =  new ArrayList<>();
                for(DataSnapshot data: snapshot.getChildren()){
                    String timeSlotID = data.getKey();
                    String recCenter = snapshot.getKey();
                    //get capacity
                    TimeSlot ts = new TimeSlot(2, recCenter , timeSlotID);
                    ts.usersCount= (int) data.getChildrenCount();
                    temp.add(ts);
                    key =  data.getKey();
                }
                rvAdapter.setItems(temp);
                rvAdapter.notifyDataSetChanged();
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