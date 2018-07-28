package com.bsm.mobile.legacy.domain.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.CalendarTime;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mlody Danon on 7/25/2017.
 */

public class CalendarTimesActivity extends AppCompatActivity {

    @BindView(R.id.simple_recycler)
    RecyclerView TimeRecycler;

    private static DatabaseReference mRootRef;

    private CalendarTimesAdapter adapter;

    private String dayId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recycler_list);
        ButterKnife.bind(this);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = this.getIntent().getExtras();
        if( bundle != null ){
            dayId = bundle.getString("day");
        }

        initializeTimeRecycler();
    }

    private void initializeTimeRecycler() {
        Query query = mRootRef.child("CalendarTimes").child(dayId).orderByChild("time");
        FirebaseRecyclerOptions<CalendarTime> options =
                new FirebaseRecyclerOptions.Builder<CalendarTime>()
                        .setQuery(query, CalendarTime.class)
                        .build();

        adapter = new CalendarTimesAdapter(options);
        TimeRecycler.setAdapter(adapter);
        TimeRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}