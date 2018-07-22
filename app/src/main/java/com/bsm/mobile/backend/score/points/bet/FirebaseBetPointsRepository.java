package com.bsm.mobile.backend.score.points.bet;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.google.firebase.database.DatabaseReference;

import static com.bsm.mobile.Constants.*;

public class FirebaseBetPointsRepository extends AbstractFirebaseRepository implements IBetPointsRepository {

    @Override
    protected DatabaseReference getRepositoryReference() {
        if(repositoryReference == null){
            repositoryReference = getRoot().child(BRANCH_BET_POINTS);
        }
        return repositoryReference;
    }

    @Override
    public void invalidateBet(String id) {
        getRepositoryReference().child(id).child(FIELD_VALID).setValue(false);
    }
}
