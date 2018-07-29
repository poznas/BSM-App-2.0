package com.bsm.mobile.backend.score.points.badge;

import com.bsm.mobile.domain.wizard.badge.model.BadgeInfo;

import java.util.List;

import io.reactivex.Observable;


public interface IBadgeInfoRepository {

    Observable<List<BadgeInfo>> getBadgeInfoList();
}
