package com.bsm.mobile.legacy.module.wizard.sm;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.bsm.mobile.legacy.model.SideMissionInfo;
import com.bsm.mobile.legacy.module.sminfo.SideMissionsInfoActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

public class AddSMListActivity extends SideMissionsInfoActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Melduj");
    }

    @Override
    protected void proceedInitializingRecycler(Query query) {
        FirebaseRecyclerOptions<SideMissionInfo> options =
                new FirebaseRecyclerOptions.Builder<SideMissionInfo>()
                        .setQuery(query, SideMissionInfo.class)
                        .build();

        adapter = new SMListAdapter(options);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
