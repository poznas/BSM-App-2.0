package com.bsm.mobile.backend.score.points.mc;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.google.firebase.database.DatabaseReference;

import static com.bsm.mobile.Constants.BRANCH_MAIN_COMPETITION_POINTS;
import static com.bsm.mobile.Constants.FIELD_VALID;

public class FirebaseMainCompetitionPointsRepository extends AbstractFirebaseRepository implements IMainCompetitionPointsRepository {

    @Override
    protected DatabaseReference getRepositoryReference() {
        if(repositoryReference == null){
            repositoryReference = getRoot().child(BRANCH_MAIN_COMPETITION_POINTS);
        }
        return repositoryReference;
    }

    @Override
    public void invalidateMainCompetition(String id) {
        getRepositoryReference().child(id).child(FIELD_VALID).setValue(false);
    }
}
