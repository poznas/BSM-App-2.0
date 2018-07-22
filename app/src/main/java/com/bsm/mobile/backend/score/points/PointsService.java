package com.bsm.mobile.backend.score.points;

import android.util.Log;

import com.bsm.mobile.TeamResources;
import com.bsm.mobile.backend.score.points.bet.IBetPointsRepository;
import com.bsm.mobile.backend.score.points.mc.IMainCompetitionPointsRepository;
import com.bsm.mobile.backend.score.points.medal.IMedalPointsRepository;
import com.bsm.mobile.backend.score.points.sm.ISideMissionPointsRepository;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.legacy.model.PointsInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.AllArgsConstructor;

import static com.bsm.mobile.Constants.*;

@AllArgsConstructor
public class PointsService implements IPointsService, Tagable{

    private final IPointsRepository pointsRepository;

    private final IBetPointsRepository betPointsRepository;
    private final IMainCompetitionPointsRepository mainCompetitionPointsRepository;
    private final IMedalPointsRepository medalPointsRepository;
    private final ISideMissionPointsRepository sideMissionPointsRepository;


    @Override
    public Observable<List<PointsInfo>> getAllPoints(String teamId) {
        if(!TeamResources.IDENTIFIERS.contains(teamId))
            return Observable.empty();

        return pointsRepository.getAllPoints()
                .observeOn(Schedulers.io())
                .map(list -> {
                    List<PointsInfo> resultList = new LinkedList<>();
                    for( PointsInfo points : list )
                        if(points.getTeam().equals(teamId)) resultList.add(points);

                    return resultList;
                });
    }

    @Override
    public void invalidatePoints(PointsInfo points) {
        Log.d(getTag(), "attempt to invalidate points : " + points);

        switch (points.getLabel()){
            case LABEL_POINTS_SIDE_MISSION:
                sideMissionPointsRepository.invalidateSideMission(points.getId());
                break;
            case LABEL_POINTS_MAIN_COMPETITION:
                mainCompetitionPointsRepository.invalidateMainCompetition(points.getId());
                break;
            case LABEL_POINTS_MEDAL:
                medalPointsRepository.invalidateMedal(points.getId());
                break;
            case LABEL_POINTS_BET:
                betPointsRepository.invalidateBet(
                        points.getPoints() > 0 ?
                                points.getId().replace("win", "") :
                                points.getId().replace("loss", "")
                );
                break;
            default:
                break;
        }
    }
}
