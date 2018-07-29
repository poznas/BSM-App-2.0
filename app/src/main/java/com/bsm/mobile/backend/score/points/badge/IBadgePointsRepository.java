package com.bsm.mobile.backend.score.points.badge;

import com.bsm.mobile.domain.wizard.badge.model.BadgePoints;

import java.util.List;

import io.reactivex.Observable;


public interface IBadgePointsRepository {

    Observable<List<BadgePoints>> getBadgePointsList();

    void invalidateBadge(String id);
}
