package com.bsm.mobile.backend.score.points.bet;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.google.firebase.database.Query;

import static com.bsm.mobile.common.resource.Constants.BRANCH_BET_POINTS;
import static com.bsm.mobile.common.resource.Constants.FIELD_VALID;

public class FirebaseBetPointsRepository extends AbstractFirebaseRepository implements IBetPointsRepository {

    @Override
    protected Query getRepositoryQuery() {
        if(repositoryQuery == null){
            repositoryQuery = getRoot().child(BRANCH_BET_POINTS);
        }
        return repositoryQuery;
    }

    @Override
    public void invalidateBet(String id) {
        getRepositoryReference().child(id).child(FIELD_VALID).setValue(false);
    }
}
