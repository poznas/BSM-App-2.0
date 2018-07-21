package com.bsm.mobile.points.list;

import com.bsm.mobile.backend.score.IScoreRepository;
import com.bsm.mobile.backend.score.points.IPointsService;
import com.bsm.mobile.backend.user.IUserAuthService;
import com.bsm.mobile.legacy.model.PointsInfo;
import com.bsm.mobile.legacy.model.User;

import java.util.List;

import io.reactivex.Observable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PointsListModel implements PointsListActivityMVP.Model {

    private IUserAuthService userAuthService;
    private IScoreRepository scoreRepository;
    private IPointsService pointsService;


    @Override
    public Observable<List<PointsInfo>> getPointsRecords(String teamId) {
        return pointsService.getAllPoints(teamId);
    }

    @Override
    public Observable<Long> getScore(String teamId) {
        return scoreRepository.getScore(teamId);
    }

    @Override
    public Observable<String> getUserLabel() {
        return userAuthService.getCurrentUser().map(User::getLabel);
    }

    @Override
    public void invalidatePoints(PointsInfo points) {
        pointsService.invalidatePoints(points);
    }
}
