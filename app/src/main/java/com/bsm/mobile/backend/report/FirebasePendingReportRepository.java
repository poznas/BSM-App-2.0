package com.bsm.mobile.backend.report;

import android.support.annotation.NonNull;

import com.bsm.mobile.Constants;
import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.bsm.mobile.model.PendingReport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

import static com.bsm.mobile.Constants.BRANCH_PENDING_REPORTS;

public class FirebasePendingReportRepository extends AbstractFirebaseRepository implements IPendingReportRepository {

    @Override
    protected DatabaseReference getRepositoryReference() {
        if(repositoryReference == null){
            repositoryReference = getRoot().child(BRANCH_PENDING_REPORTS);
        }
        return repositoryReference;
    }

    @Override
    public Observable<Map<String, PendingReport>> getPendingReports() {

        return Observable.create(emitter -> {

            new AbstractValueEventListener<Map<String, PendingReport>>(emitter, getRepositoryReference()){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, PendingReport> pendingReports = new HashMap<>();

                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        pendingReports.put(child.getKey(), child.getValue(PendingReport.class));
                    }
                    emitter.onNext(pendingReports);
                }
            };
        });
    }
}
