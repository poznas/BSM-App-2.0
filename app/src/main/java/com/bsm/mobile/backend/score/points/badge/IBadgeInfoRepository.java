package com.bsm.mobile.backend.score.points.badge;

import com.bsm.mobile.domain.wizard.badge.model.BadgeInfo;

import java.util.List;

import io.reactivex.Single;


public interface IBadgeInfoRepository {

    Single<List<BadgeInfo>> getBadgeInfoList();
}
