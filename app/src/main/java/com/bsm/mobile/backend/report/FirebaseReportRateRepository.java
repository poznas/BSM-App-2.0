package com.bsm.mobile.backend.report;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.google.firebase.database.DatabaseReference;

import static com.bsm.mobile.Constants.BRANCH_REPORT_RATES;

public class FirebaseReportRateRepository extends AbstractFirebaseRepository implements IReportRateRepository {

    @Override
    protected DatabaseReference getRepositoryReference() {
        if(repositoryReference == null){
            repositoryReference = getRoot().child(BRANCH_REPORT_RATES);
        }
        return repositoryReference;
    }
}
