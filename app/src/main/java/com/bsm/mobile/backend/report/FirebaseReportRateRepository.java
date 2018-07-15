package com.bsm.mobile.backend.report;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.Observable;

import static com.bsm.mobile.Constants.BRANCH_REPORT_RATES;

public class FirebaseReportRateRepository extends AbstractFirebaseRepository implements IReportRateRepository {

    @Override
    protected DatabaseReference getRepositoryReference() {
        if(repositoryReference == null){
            repositoryReference = getRoot().child(BRANCH_REPORT_RATES);
        }
        return repositoryReference;
    }

    @Override
    public Observable<Set<String>> getReportJudgeRatersIds(String reportId) {

        return Observable.create(emitter -> {

            new AbstractValueEventListener<Set<String>>(emitter, getRepositoryReference().child(reportId)){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Set<String> judgeIds = new HashSet<>();
                    for( DataSnapshot judgeRate : dataSnapshot.getChildren()){
                        judgeIds.add(judgeRate.getKey());
                    }
                    Log.d(getTag(), "retrieved raters ids : " + judgeIds);
                    emitter.onNext(judgeIds);
                }
            };
        });
    }
}
