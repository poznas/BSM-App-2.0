package com.bsm.mobile.backend.score.points;

import com.bsm.mobile.Constants;
import com.bsm.mobile.TeamResources;
import com.bsm.mobile.legacy.model.PointsInfo;

import java.util.List;

import io.reactivex.Observable;
import lombok.AllArgsConstructor;

import static com.bsm.mobile.Constants.*;

@AllArgsConstructor
public class PointsService implements IPointsService{

    private final IPointsRepository pointsRepository;


    @Override
    public Observable<List<PointsInfo>> getAllPoints(String teamId) {
        if(!TeamResources.IDENTIFIERS.contains(teamId))
            return Observable.empty();

        return pointsRepository.getAllPoints(teamId);
    }

    @Override
    public void invalidatePoints(PointsInfo points) {
        switch (points.getLabel()){
            case LABEL_POINTS_SIDE_MISSION:
                pointsRepository.invalidateSideMission(points.getId());
                break;
            case LABEL_POINTS_MAIN_COMPETITION:
                pointsRepository.invalidateMainCompetition(points.getId());
                break;
            case LABEL_POINTS_MEDAL:
                pointsRepository.invalidateMedal(points.getId());
                break;
            case LABEL_POINTS_BET:
                pointsRepository.invalidateBet(
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
