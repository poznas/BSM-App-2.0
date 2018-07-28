package com.bsm.mobile.backend.report;

import com.bsm.mobile.common.resource.Constants;
import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.google.firebase.database.DatabaseReference;

import io.reactivex.Observable;
import io.reactivex.Single;

public class FirebaseReportRepository extends AbstractFirebaseRepository implements IReportRepository{

    @Override
    protected DatabaseReference getRepositoryQuery() {
        return getRoot().child(Constants.BRANCH_REPORT_LOCK);
    }

    @Override
    public Single<Boolean> setReportLockState(boolean unlocked) {
        return Single.create(emitter -> getRepositoryQuery().setValue(unlocked)
                .addOnCompleteListener(task -> emitter.onSuccess(task.isSuccessful())));
    }

    @Override
    public Observable<Boolean> getReportLockState() {
        return Observable.create(emitter ->
                new SimpleValueEventListener(emitter, getRepositoryQuery())
                .setOnDataChange(
                        dataSnapshot ->
                        emitter.onNext(dataSnapshot.getValue(Boolean.class) != null ?
                                dataSnapshot.getValue(Boolean.class) : false
                        )
                )
        );
    }


}
