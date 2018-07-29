package com.bsm.mobile.domain.wizard.badge;

import com.bsm.mobile.backend.score.points.badge.IBadgeInfoRepository;
import com.bsm.mobile.backend.score.points.badge.IBadgePointsRepository;
import com.bsm.mobile.domain.wizard.badge.BadgeInfoActivityMVP.Model;
import com.bsm.mobile.domain.wizard.badge.model.BadgeInfo;
import com.bsm.mobile.domain.wizard.badge.model.BadgeInfoView;
import com.bsm.mobile.domain.wizard.badge.model.BadgePoints;
import com.bsm.mobile.legacy.model.User;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BadgeInfoModel implements Model {

    private final IBadgePointsRepository badgePointsRepository;
    private final IBadgeInfoRepository badgeInfoRepository;


    @Override
    public Observable<List<BadgeInfoView>> getBadgeInfoList() {

        return badgePointsRepository.getBadgePointsList()
                .observeOn(Schedulers.computation())
                .map(pointsList -> {
                    List<BadgeInfoView> views = new LinkedList<>();
                    List<BadgeInfo> infoList = badgeInfoRepository.getBadgeInfoList()
                            .observeOn(Schedulers.io()).blockingGet();

                    for(BadgeInfo info : infoList){
                        BadgeInfoView view = BadgeInfoView.builder()
                                .awardedUsers(new LinkedList<>())
                                .badgeInfo(info)
                                .build();

                        for(BadgePoints points : pointsList)
                            if(points.getSideMissionName().equals(info.getSideMissionName()))
                                view.getAwardedUsers().add(
                                        User.builder()
                                                .photoUrl(points.getUserPhotoUrl())
                                                .displayName(points.getUserName())
                                                .team(points.getTeam())
                                                .build());
                    }
                    return views;
                });
    }
}
