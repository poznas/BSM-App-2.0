package com.bsm.mobile.backend.report;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bsm.mobile.Constants;
import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.bsm.mobile.legacy.model.PendingReport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

import static com.bsm.mobile.Constants.BRANCH_PENDING_REPORTS;
import static com.bsm.mobile.Constants.BRANCH_REQUIRE_PROFESSOR_RATE_REPORTS;

public class FirebasePendingReportRepository extends AbstractFirebaseRepository implements IPendingReportRepository {

    @Override
    protected DatabaseReference getRepositoryReference() {
        if(repositoryReference == null){
            repositoryReference = getRoot().child(BRANCH_PENDING_REPORTS);
        }
        return repositoryReference;
    }

    @Override
    public Observable<Map<String, PendingReport>> getProfessorPendingReports() {

        return getPendingReports(getRoot().child(BRANCH_REQUIRE_PROFESSOR_RATE_REPORTS));
    }

    /**
     * @return all pending reports still requiring more judge's rates
     */
    @Override
    public Observable<Map<String, PendingReport>> getJudgePendingReports() {

        return getPendingReports(getRepositoryReference());
    }

    private Observable<Map<String, PendingReport>> getPendingReports(DatabaseReference reference) {

        return Observable.create(emitter -> {

            new AbstractValueEventListener<Map<String, PendingReport>>(emitter, reference){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, PendingReport> pendingReports = new HashMap<>();

                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        PendingReport pendingReport = child.getValue(PendingReport.class);
                        pendingReport.setRpid(child.getKey());

                        pendingReports.put(child.getKey(), pendingReport);
                    }
                    Log.d(getTag(), "GET pending reports : " + pendingReports);
                    emitter.onNext(pendingReports);
                }
            };
        });
    }
}
