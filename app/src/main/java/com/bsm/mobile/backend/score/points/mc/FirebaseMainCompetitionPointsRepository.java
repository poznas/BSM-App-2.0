package com.bsm.mobile.backend.score.points.mc;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.google.firebase.database.Query;

import static com.bsm.mobile.common.resource.Constants.BRANCH_MAIN_COMPETITION_POINTS;
import static com.bsm.mobile.common.resource.Constants.FIELD_VALID;

public class FirebaseMainCompetitionPointsRepository extends AbstractFirebaseRepository implements IMainCompetitionPointsRepository {

    @Override
    protected Query getRepositoryQuery() {
        if(repositoryQuery == null){
            repositoryQuery = getRoot().child(BRANCH_MAIN_COMPETITION_POINTS);
        }
        return repositoryQuery;
    }

    @Override
    public void invalidateMainCompetition(String id) {
        getRepositoryReference().child(id).child(FIELD_VALID).setValue(false);
    }
}
