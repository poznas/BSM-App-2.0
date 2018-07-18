package com.bsm.mobile.legacy.module.wizard.sm.list;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.bsm.mobile.backend.user.FirebaseUserAuthService;
import com.bsm.mobile.backend.user.FirebaseUserRepository;
import com.bsm.mobile.legacy.model.SideMissionInfo;
import com.bsm.mobile.legacy.module.info.sm.SideMissionsInfoActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddSMListActivity extends SideMissionsInfoActivity {

    Disposable teamSubscription;

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

    @Override
    protected void onStart() {
        super.onStart();
        teamSubscription = new FirebaseUserAuthService(new FirebaseUserRepository())
                .getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    ((SMListAdapter) adapter).setTeam(user.getTeam());
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        teamSubscription.dispose();
    }
}
