package com.caffeinatedfingers.myuscrecapp.InstrumentedTests;

import static org.junit.Assert.*;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.platform.app.InstrumentationRegistry;

import com.caffeinatedfingers.myuscrecapp.DAOFireBase;
import com.caffeinatedfingers.myuscrecapp.R;
import com.caffeinatedfingers.myuscrecapp.RVAdapter;
import com.caffeinatedfingers.myuscrecapp.RecCenter;
import com.caffeinatedfingers.myuscrecapp.TimeSlot;
import com.caffeinatedfingers.myuscrecapp.User;

import org.junit.Before;
import org.junit.Test;

public class RVAdapterTest {
    private RecCenter recCenter;
    private DAOFireBase dao;
    private String date;
    private RVAdapter rvAdapter;
    private User user;
    private boolean isLoading;
    private TimeSlot timeSlot;
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    @Before
    public void setUp() {
        LinearLayoutManager manager = new LinearLayoutManager(appContext);
        user = new User("00000000", "TEST","uid");
        recCenter = new RecCenter("GYMTEST", "INFO");
        date ="TODAY";
        isLoading= false;
        timeSlot = new TimeSlot(5L, "GYMTEST", "10-11AM",date);
        dao = new DAOFireBase();
        rvAdapter = new RVAdapter(appContext, dao, user);

    }
    @Test
    public void add() {
        rvAdapter.add(timeSlot);
        assertEquals(1, rvAdapter.getItemCount());
        assertEquals(timeSlot,rvAdapter.getTimeSlot(timeSlot.getId()));
    }

    @Test
    public void addDuplicates() {
        rvAdapter.add(timeSlot);
        rvAdapter.add(timeSlot);
        assertEquals(1, rvAdapter.getItemCount());
        assertEquals(timeSlot,rvAdapter.getTimeSlot(timeSlot.getId()));
    }

    @Test
    public void delete() {
        rvAdapter.add(timeSlot);
        rvAdapter.add(timeSlot);
        rvAdapter.delete(timeSlot);
        assertEquals(0,rvAdapter.getItemCount());
        assertNull(rvAdapter.getTimeSlot(timeSlot.getId()));
    }

    @Test
    public void refreshDelete() {
        rvAdapter.add(timeSlot);
        rvAdapter.refreshDelete();
        assertEquals(0, rvAdapter.getItemCount());
    }

}