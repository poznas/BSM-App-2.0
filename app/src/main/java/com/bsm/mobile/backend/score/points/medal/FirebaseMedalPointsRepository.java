package com.bsm.mobile.backend.score.points.medal;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.google.firebase.database.DatabaseReference;

import static com.bsm.mobile.Constants.BRANCH_MEDAL_POINTS;
import static com.bsm.mobile.Constants.FIELD_VALID;

public class FirebaseMedalPointsRepository extends AbstractFirebaseRepository implements IMedalPointsRepository {

    @Override
    protected DatabaseReference getRepositoryReference() {
        if(repositoryReference == null){
            repositoryReference = getRoot().child(BRANCH_MEDAL_POINTS);
        }
        return repositoryReference;
    }

    @Override
    public void invalidateMedal(String id) {
        getRepositoryReference().child(id).child(FIELD_VALID).setValue(false);
    }
}
