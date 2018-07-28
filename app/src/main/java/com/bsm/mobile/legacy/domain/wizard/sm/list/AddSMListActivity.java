package com.bsm.mobile.legacy.domain.wizard.sm.list;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.bsm.mobile.backend.report.IReportRepository;
import com.bsm.mobile.backend.user.IUserAuthService;
import com.bsm.mobile.legacy.domain.info.sm.SideMissionsInfoActivity;
import com.bsm.mobile.legacy.model.SideMissionInfo;
import com.bsm.mobile.root.App;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddSMListActivity extends SideMissionsInfoActivity {

    @Inject
    IUserAuthService userAuthService;
    @Inject
    IReportRepository reportRepository;

    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getComponent().inject(this);
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
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onStart() {
        super.onStart();
        compositeDisposable.add(
                userAuthService
                .getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(user -> ((SMListAdapter) adapter).setTeam(user.getTeam())));
        compositeDisposable.add(
          reportRepository.getReportLockState()
                  .subscribeOn(Schedulers.io())
                  .observeOn(Schedulers.single())
                  .subscribe(unlocked -> ((SMListAdapter) adapter).setReportsEnabled(unlocked))
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.dispose();
    }
}
