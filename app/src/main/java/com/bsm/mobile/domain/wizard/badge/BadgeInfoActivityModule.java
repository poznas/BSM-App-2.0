package com.bsm.mobile.domain.wizard.badge;

import com.bsm.mobile.backend.score.points.badge.IBadgeInfoRepository;
import com.bsm.mobile.backend.score.points.badge.IBadgePointsRepository;
import com.bsm.mobile.domain.wizard.badge.BadgeInfoActivityMVP.Model;
import com.bsm.mobile.domain.wizard.badge.BadgeInfoActivityMVP.Presenter;

import dagger.Module;
import dagger.Provides;

@Module
public class BadgeInfoActivityModule {

    @Provides
    public Model provideBadgeInfoModel(IBadgeInfoRepository badgeInfoRepository,
                                       IBadgePointsRepository badgePointsRepository){
        return new BadgeInfoModel(badgePointsRepository, badgeInfoRepository);
    }

    @Provides
    public Presenter provideBadgeInfoPresenter(Model model){
        return new BadgeInfoPresenter(model);
    }
}
