package com.bsm.mobile.backend.score.points.badge;

import android.util.Log;

import com.bsm.mobile.backend.AbstractFirebaseRepository;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.domain.wizard.badge.model.BadgeInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;

import static com.bsm.mobile.common.resource.Constants.BRANCH_BADGE_INFO;

public class FirebaseBadgeInfoRepository extends AbstractFirebaseRepository implements IBadgeInfoRepository, Tagable {

    @Override
    protected Query getRepositoryQuery() {
        return getRoot().child(BRANCH_BADGE_INFO);
    }

    @Override
    public Observable<List<BadgeInfo>> getBadgeInfoList() {
        return Observable.create(emitter ->
                new SimpleValueEventListener(emitter, getRepositoryQuery())
                    .setOnDataChange(dataSnapshot -> {
                        List<BadgeInfo> infoList = new LinkedList<>();
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            BadgeInfo info = child.getValue(BadgeInfo.class);

                            if(info != null){
                                info.setSideMissionName(child.getKey());
                                infoList.add(info);
                            }
                        }
                        Log.d(getTag(), "retrieved badge info : " + infoList);
                        emitter.onNext(infoList);
                    })
        );
    }

    @Override
    public Observable<BadgeInfo> getBadgeInfo(String sideMissionName) {
        return Observable.create(emitter ->
            new SimpleValueEventListener(emitter, getRepositoryReference().child(sideMissionName))
                .setOnDataChange(dataSnapshot -> {
                   BadgeInfo badgeInfo = dataSnapshot.getValue(BadgeInfo.class);
                   if(badgeInfo != null) emitter.onNext(badgeInfo);
                })
        );
    }
}
