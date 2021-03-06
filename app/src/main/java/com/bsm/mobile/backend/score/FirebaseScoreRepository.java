package com.bsm.mobile.backend.score;

import com.bsm.mobile.common.resource.TeamResources;
import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.google.firebase.database.Query;

import java.util.HashMap;

import io.reactivex.Observable;

import static com.bsm.mobile.common.resource.Constants.BRANCH_SCORES;
import static com.bsm.mobile.common.resource.Constants.TEAM_CORMEUM;
import static com.bsm.mobile.common.resource.Constants.TEAM_MUTINIUM;
import static com.bsm.mobile.common.resource.Constants.TEAM_SENSUM;

public class FirebaseScoreRepository extends AbstractFirebaseRepository implements IScoreRepository {

    @Override
    public Observable<HashMap<String, Long>> getScores() {

        return Observable.create(emitter -> {

            new SimpleValueEventListener(emitter, getRepositoryQuery())
                    .setOnDataChange(dataSnapshot -> {
                        HashMap<String, Long> map = new HashMap<>();
                        map.put(TEAM_CORMEUM, dataSnapshot.child(TEAM_CORMEUM).getValue(Long.class));
                        map.put(TEAM_SENSUM, dataSnapshot.child(TEAM_SENSUM).getValue(Long.class));
                        map.put(TEAM_MUTINIUM, dataSnapshot.child(TEAM_MUTINIUM).getValue(Long.class));

                        emitter.onNext(map);
                    });
        });
    }

    @Override
    public Observable<Long> getScore(String teamId) {

        if(!TeamResources.IDENTIFIERS.contains(teamId))
            return Observable.empty();

        return getScores().map(scores -> scores.get(teamId));
    }

    @Override
    protected Query getRepositoryQuery() {
        if(repositoryQuery == null){
            repositoryQuery = getRoot().child(BRANCH_SCORES);
        }
        return repositoryQuery;
    }
}
