package com.bsm.mobile.backend.score.points.sm;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.google.firebase.database.Query;

import static com.bsm.mobile.common.resource.Constants.BRANCH_REPORTS;
import static com.bsm.mobile.common.resource.Constants.BRANCH_SIDE_MISSION_POINTS;
import static com.bsm.mobile.common.resource.Constants.FIELD_VALID;

public class FirebaseSideMissionPointsRepository extends AbstractFirebaseRepository implements ISideMissionPointsRepository {

    @Override
    protected Query getRepositoryQuery() {
        if(repositoryQuery == null){
            repositoryQuery = getRoot().child(BRANCH_SIDE_MISSION_POINTS);
        }
        return repositoryQuery;
    }

    @Override
    public void invalidateSideMission(String id) {
        getRoot().child(BRANCH_REPORTS).child(id).child(FIELD_VALID).setValue(false);
    }
}
