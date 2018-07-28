package com.bsm.mobile.backend.report;

import android.util.Log;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.Observable;

import static com.bsm.mobile.common.resource.Constants.BRANCH_REPORT_RATES;

public class FirebaseReportRateRepository extends AbstractFirebaseRepository implements IReportRateRepository {

    @Override
    protected Query getRepositoryQuery() {
        if(repositoryQuery == null){
            repositoryQuery = getRoot().child(BRANCH_REPORT_RATES);
        }
        return repositoryQuery;
    }

    @Override
    public Observable<Set<String>> getReportJudgeRatersIds(String reportId) {

        return Observable.create(emitter -> {

            new SimpleValueEventListener(emitter, getRepositoryReference().child(reportId))
                    .setOnDataChange(dataSnapshot -> {
                        Set<String> judgeIds = new HashSet<>();
                        for( DataSnapshot judgeRate : dataSnapshot.getChildren()){
                            judgeIds.add(judgeRate.getKey());
                        }
                        Log.d(getTag(), "retrieved raters ids : " + judgeIds);
                        emitter.onNext(judgeIds);
                    });
        });
    }
}
