package com.bsm.mobile.backend.score.points.badge;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.bsm.mobile.domain.wizard.badge.model.BadgePoints;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;

import static com.bsm.mobile.common.resource.Constants.BRANCH_BADGE_POINTS;
import static com.bsm.mobile.common.resource.Constants.FIELD_VALID;

public class FirebaseBadgePointsRepository extends AbstractFirebaseRepository implements IBadgePointsRepository {

    @Override
    protected Query getRepositoryQuery() {
        return getRoot().child(BRANCH_BADGE_POINTS);
    }

    @Override
    public Observable<List<BadgePoints>> getBadgePointsList() {

        return Observable.create(emitter ->
                new SimpleValueEventListener(emitter, getRepositoryQuery())
                    .setOnDataChange(dataSnapshot -> {
                        List<BadgePoints> pointsList = new LinkedList<>();
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            BadgePoints points = child.getValue(BadgePoints.class);

                            if(points != null){
                                points.setBadgeId(child.getKey());
                                pointsList.add(points);
                            }
                        }
                        emitter.onNext(pointsList);
                    })
        );
    }

    @Override
    public void invalidateBadge(String id) {
        getRepositoryReference().child(id).child(FIELD_VALID).setValue(false);
    }
}
