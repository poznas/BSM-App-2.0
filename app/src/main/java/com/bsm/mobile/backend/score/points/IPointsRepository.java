package com.bsm.mobile.backend.score.points;

import com.bsm.mobile.legacy.model.PointsInfo;

import java.util.List;

import io.reactivex.Observable;

public interface IPointsRepository {
    Observable<List<PointsInfo>> getAllPoints(String teamId);

    void invalidateSideMission(String id);

    void invalidateMainCompetition(String id);

    void invalidateMedal(String id);

    void invalidateBet(String replace);
}
