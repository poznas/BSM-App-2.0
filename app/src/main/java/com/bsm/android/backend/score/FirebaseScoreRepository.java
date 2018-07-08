package com.bsm.android.backend.score;

import android.support.annotation.NonNull;

import com.bsm.android.Constants;
import com.bsm.android.backend.AbstractFirebaseRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.reactivex.Observable;

import static com.bsm.android.Constants.TEAM_CORMEUM;
import static com.bsm.android.Constants.TEAM_MUTINIUM;
import static com.bsm.android.Constants.TEAM_SENSUM;

public class FirebaseScoreRepository extends AbstractFirebaseRepository implements IScoreRepository {

    private ValueEventListener listener;
    private DatabaseReference scoreReference;

    @Override
    public Observable<HashMap<String, Long>> getScoresStream() {

        return Observable.create(emitter -> {
           shutdown();
           listener = new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   HashMap<String, Long> map = new HashMap<>();
                   map.put(TEAM_CORMEUM, dataSnapshot.child(TEAM_CORMEUM).getValue(Long.class));
                   map.put(TEAM_SENSUM, dataSnapshot.child(TEAM_SENSUM).getValue(Long.class));
                   map.put(TEAM_MUTINIUM, dataSnapshot.child(TEAM_MUTINIUM).getValue(Long.class));

                   emitter.onNext(map);
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {
                    emitter.onError(databaseError.toException());
               }
           };
        });
    }

    @Override
    public void shutdown() {
        if(listener != null){
            getScoreReference().removeEventListener(listener);
        }
    }

    private DatabaseReference getScoreReference() {
        if(scoreReference == null){
            scoreReference = getRoot().child(Constants.BRANCH_SCORES);
        }
        return scoreReference;
    }
}
