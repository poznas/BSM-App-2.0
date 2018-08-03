package com.bsm.mobile.domain.points.ranking;

import com.bsm.mobile.backend.score.points.IPointsService;
import com.bsm.mobile.backend.user.IUserRepository;
import com.bsm.mobile.common.utils.UserDataValidator;
import com.bsm.mobile.legacy.model.PointsInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;

import static com.bsm.mobile.common.resource.Constants.LABEL_POINTS_SIDE_MISSION;
import static com.bsm.mobile.common.utils.PointsInfoUtils.directWizardPoints;
import static com.bsm.mobile.domain.points.ranking.SideMissionRankingActivityMVP.Model;

@RequiredArgsConstructor
public class SideMissionRankingModel implements Model {

    private final IPointsService pointsService;
    private final IUserRepository userRepository;

    @Override
    public Observable<List<PointsInfo>> getRanking() {
        return pointsService.getAllPoints()
                .observeOn(Schedulers.io())
                .map(pointsList -> {

                    Map<String, PointsInfo> wizardPointsMap = getWizardZeroPointsMap();
                    for(PointsInfo pointsInfo : pointsList){
                        if(!directWizardPoints(pointsInfo)) continue;

                        PointsInfo userPointsRecord = wizardPointsMap.get(pointsInfo.getUser_name());
                        if (userPointsRecord != null) {
                            userPointsRecord.setPoints(
                                    userPointsRecord.getPoints() + pointsInfo.getPoints());
                        }
                    }
                    return toList(wizardPointsMap);
                })
                .observeOn(Schedulers.computation())
                .doOnEach(points -> Collections.sort(points.getValue()));

    }

    private Map<String, PointsInfo> getWizardZeroPointsMap() {
        return userRepository.getUserList().take(1)
                .observeOn(Schedulers.io())
                .flatMapIterable(users -> users)
                .filter(UserDataValidator::isWizard)
                .map(user -> PointsInfo.builder()
                        .label(LABEL_POINTS_SIDE_MISSION)
                        .user_photo(user.getPhotoUrl())
                        .user_name(user.getDisplayName())
                        .team(user.getTeam())
                        .points(0L)
                        .build())
                .toMap(PointsInfo::getUser_name, pointsInfo -> pointsInfo)
                .blockingGet();
    }

    private List<PointsInfo> toList(Map<String, PointsInfo> userPointsMap){
        return new ArrayList<>(userPointsMap.values());
    }
}
