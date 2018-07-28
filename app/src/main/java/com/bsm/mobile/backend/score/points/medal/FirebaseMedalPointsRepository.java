package com.bsm.mobile.backend.score.points.medal;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.google.firebase.database.Query;

import static com.bsm.mobile.common.resource.Constants.BRANCH_MEDAL_POINTS;
import static com.bsm.mobile.common.resource.Constants.FIELD_VALID;

public class FirebaseMedalPointsRepository extends AbstractFirebaseRepository implements IMedalPointsRepository {

    @Override
    protected Query getRepositoryQuery() {
        if(repositoryQuery == null){
            repositoryQuery = getRoot().child(BRANCH_MEDAL_POINTS);
        }
        return repositoryQuery;
    }

    @Override
    public void invalidateMedal(String id) {
        getRepositoryReference().child(id).child(FIELD_VALID).setValue(false);
    }
}
