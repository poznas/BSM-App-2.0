package com.bsm.mobile.backend.score;

import android.support.annotation.NonNull;

import com.bsm.mobile.Constants;
import com.bsm.mobile.TeamResources;
import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.reactivex.Observable;

import static com.bsm.mobile.Constants.*;
import static com.bsm.mobile.Constants.TEAM_CORMEUM;
import static com.bsm.mobile.Constants.TEAM_MUTINIUM;
import static com.bsm.mobile.Constants.TEAM_SENSUM;

public class FirebaseScoreRepository extends AbstractFirebaseRepository implements IScoreRepository {

    @Override
    public Observable<HashMap<String, Long>> getScores() {

        return Observable.create(emitter -> {

           new AbstractValueEventListener<HashMap<String, Long>>(emitter, getRepositoryReference()) {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   HashMap<String, Long> map = new HashMap<>();
                   map.put(TEAM_CORMEUM, dataSnapshot.child(TEAM_CORMEUM).getValue(Long.class));
                   map.put(TEAM_SENSUM, dataSnapshot.child(TEAM_SENSUM).getValue(Long.class));
                   map.put(TEAM_MUTINIUM, dataSnapshot.child(TEAM_MUTINIUM).getValue(Long.class));

                   emitter.onNext(map);
               }
           };
        });
    }

    @Override
    public Observable<Long> getScore(String teamId) {

        if(!TeamResources.IDENTIFIERS.contains(teamId))
            return Observable.empty();

        return getScores().map(scores -> scores.get(teamId));
    }

    @Override
    protected DatabaseReference getRepositoryReference() {
        if(repositoryReference == null){
            repositoryReference = getRoot().child(BRANCH_SCORES);
        }
        return repositoryReference;
    }
}
