package com.bsm.mobile.backend.report;

import android.util.Log;

import com.bsm.mobile.Constants;
import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.bsm.mobile.legacy.model.PendingReport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

import static com.bsm.mobile.Constants.BRANCH_PENDING_REPORTS;
import static com.bsm.mobile.Constants.BRANCH_REQUIRE_PROFESSOR_RATE_REPORTS;
import static com.bsm.mobile.Constants.FIELD_TIMESTAMP;

public class FirebasePendingReportRepository extends AbstractFirebaseRepository implements IPendingReportRepository {

    @Override
    protected Query getRepositoryReference() {
        if(repositoryReference == null){
            repositoryReference = getRoot()
                    .child(BRANCH_PENDING_REPORTS)
                    .orderByChild(Constants.FIELD_TIMESTAMP);
        }
        return repositoryReference;
    }

    @Override
    public Observable<Map<String, PendingReport>> getProfessorPendingReports() {

        return getPendingReports(getRoot()
                .child(BRANCH_REQUIRE_PROFESSOR_RATE_REPORTS)
                .orderByChild(FIELD_TIMESTAMP));
    }

    /**
     * @return all pending reports still requiring more judge's rates
     */
    @Override
    public Observable<Map<String, PendingReport>> getJudgePendingReports() {

        return getPendingReports(getRepositoryReference());
    }

    private Observable<Map<String, PendingReport>> getPendingReports(Query reference) {

        return Observable.create(emitter -> {

            new SimpleValueEventListener(emitter, reference)
                    .setOnDataChange(dataSnapshot -> {
                        Map<String, PendingReport> pendingReports = new HashMap<>();

                        for (DataSnapshot child : dataSnapshot.getChildren()){
                            PendingReport pendingReport = child.getValue(PendingReport.class);
                            if (pendingReport == null) continue;

                            pendingReport.setRpid(child.getKey());
                            pendingReports.put(child.getKey(), pendingReport);
                        }
                        Log.d(getTag(), "GET pending reports : " + pendingReports);
                        emitter.onNext(pendingReports);
                    });
        });
    }
}
