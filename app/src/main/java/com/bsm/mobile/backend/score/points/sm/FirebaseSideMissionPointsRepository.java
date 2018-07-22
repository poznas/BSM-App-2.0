package com.bsm.mobile.backend.score.points.sm;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.google.firebase.database.DatabaseReference;

import static com.bsm.mobile.Constants.BRANCH_REPORTS;
import static com.bsm.mobile.Constants.BRANCH_SIDE_MISSION_POINTS;
import static com.bsm.mobile.Constants.FIELD_VALID;

public class FirebaseSideMissionPointsRepository extends AbstractFirebaseRepository implements ISideMissionPointsRepository {

    @Override
    protected DatabaseReference getRepositoryReference() {
        if(repositoryReference == null){
            repositoryReference = getRoot().child(BRANCH_SIDE_MISSION_POINTS);
        }
        return repositoryReference;
    }

    @Override
    public void invalidateSideMission(String id) {
        getRoot().child(BRANCH_REPORTS).child(id).child(FIELD_VALID).setValue(false);
    }
}
