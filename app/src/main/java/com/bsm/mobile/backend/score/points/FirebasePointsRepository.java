package com.bsm.mobile.backend.score.points;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.bsm.mobile.legacy.model.PointsInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import lombok.AllArgsConstructor;

import static com.bsm.mobile.Constants.BRANCH_ALL_POINTS;
import static com.bsm.mobile.Constants.FIELD_TIMESTAMP;

@AllArgsConstructor
public class FirebasePointsRepository extends AbstractFirebaseRepository implements IPointsRepository {

    @Override
    protected DatabaseReference getRepositoryReference() {
        return getRoot().child(BRANCH_ALL_POINTS);
    }

    @Override
    public Observable<List<PointsInfo>> getAllPoints() {

        return Observable.create(emitter -> {

            Query query = getRepositoryReference().orderByChild(FIELD_TIMESTAMP);

            new SimpleValueEventListener(emitter, query)
                    .setOnDataChange(dataSnapshot -> {
                        List<PointsInfo> points = new ArrayList<>();
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            PointsInfo pointsInfo = child.getValue(PointsInfo.class);

                            if( pointsInfo != null ){
                                pointsInfo.setId(child.getKey());
                                points.add(pointsInfo);
                            }
                        }
                        emitter.onNext(points);
                    });
        });
    }
}
