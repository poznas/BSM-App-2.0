package com.bsm.mobile.backend.score.points;

import com.bsm.mobile.legacy.model.PointsInfo;

import java.util.List;

import io.reactivex.Observable;

public class PointsRepository implements IPointsRepository {

    @Override //TODO: attach id
    public Observable<List<PointsInfo>> getAllPoints(String teamId) {
        return null;
    }

    @Override
    public void invalidateSideMission(String id) {

    }

    @Override
    public void invalidateMainCompetition(String id) {

    }

    @Override
    public void invalidateMedal(String id) {

    }

    @Override
    public void invalidateBet(String replace) {

    }

}
