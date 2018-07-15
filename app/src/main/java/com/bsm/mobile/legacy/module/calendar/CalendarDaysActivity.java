package com.bsm.mobile.legacy.module.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.CalendarDay;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mlody Danon on 7/25/2017.
 */

public class CalendarDaysActivity extends AppCompatActivity {

    @BindView(R.id.simple_recycler)
    RecyclerView DayRecycler;

    private DatabaseReference mRootRef;

    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recycler_list);
        ButterKnife.bind(this);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        initializeDaysRecycler();
    }

    private void initializeDaysRecycler() {
        Query query = mRootRef.child("CalendarDays").orderByChild("timestamp");
        FirebaseRecyclerOptions<CalendarDay> options =
                new FirebaseRecyclerOptions.Builder<CalendarDay>()
                        .setQuery(query, CalendarDay.class)
                        .build();

        adapter = new CalendarDaysAdapter(options);
        DayRecycler.setAdapter(adapter);
        DayRecycler.setLayoutManager(new LinearLayoutManager(this));
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
