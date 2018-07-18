package com.bsm.mobile.legacy.module.sminfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.SideMissionInfo;
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

public class SideMissionsInfoActivity extends AppCompatActivity {

    protected FirebaseRecyclerAdapter adapter;

    @BindView(R.id.simple_recycler)
    protected RecyclerView recyclerView;

    protected DatabaseReference mRootRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recycler_list);
        ButterKnife.bind(this);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        initializeInfoRecycler();
    }


    protected void initializeInfoRecycler() {
        Query query = mRootRef.child("SideMissionsDocs").orderByChild("name");
        proceedInitializingRecycler(query);
    }

    protected void proceedInitializingRecycler(Query query) {
        FirebaseRecyclerOptions<SideMissionInfo> options =
                new FirebaseRecyclerOptions.Builder<SideMissionInfo>()
                        .setQuery(query, SideMissionInfo.class)
                        .build();

        adapter = new GoogleDriveLinkAdapter(options);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
