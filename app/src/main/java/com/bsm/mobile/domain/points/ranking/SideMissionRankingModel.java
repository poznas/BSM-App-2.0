package com.bsm.mobile.domain.points.ranking;

import com.bsm.mobile.backend.score.points.IPointsService;
import com.bsm.mobile.common.resource.Constants;
import com.bsm.mobile.legacy.model.PointsInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;

import static com.bsm.mobile.domain.points.ranking.SideMissionRankingActivityMVP.Model;

@RequiredArgsConstructor
public class SideMissionRankingModel implements Model {

    private final IPointsService pointsService;

    @Override
    public Observable<List<PointsInfo>> getRanking() {
        return pointsService.getAllPoints()
                .observeOn(Schedulers.computation())
                .map(pointsList -> {
                    HashMap<String, PointsInfo> userPointsMap = new HashMap<>();
                    for(PointsInfo pointsInfo : pointsList){
                        if(!pointsInfo.getLabel().equals(Constants.LABEL_POINTS_SIDE_MISSION)) continue;

                        PointsInfo existingUserPointsRecord = userPointsMap.get(pointsInfo.getUser_name());

                        if(existingUserPointsRecord == null){
                            userPointsMap.put(pointsInfo.getUser_name(),
                                    PointsInfo.builder()
                                            .user_name(pointsInfo.getUser_name())
                                            .team(pointsInfo.getTeam())
                                            .user_photo(pointsInfo.getUser_photo())
                                            .points(pointsInfo.getPoints())
                                            .build());
                        }else{
                            existingUserPointsRecord.setPoints(
                                    existingUserPointsRecord.getPoints() + pointsInfo.getPoints());
                        }
                    }
                    List<PointsInfo> result = new ArrayList<>(userPointsMap.values());
                    return result;
                })
                .observeOn(Schedulers.computation())
                .doOnEach(points -> Collections.sort(points.getValue()));

    }
}
