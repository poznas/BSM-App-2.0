package com.bsm.mobile.domain.points.ranking;


import com.bsm.mobile.backend.score.points.IPointsService;
import com.bsm.mobile.domain.points.ranking.SideMissionRankingActivityMVP.Model;
import com.bsm.mobile.domain.points.ranking.SideMissionRankingActivityMVP.Presenter;

import dagger.Module;
import dagger.Provides;

@Module
public class SideMissionRankingActivityModule {

    @Provides
    public Model provideSideMissionRankingModel(IPointsService pointsService){
        return new SideMissionRankingModel(pointsService);
    }

    @Provides
    Presenter provideSideMissionRankingPresenter(Model model){
        return new SideMissionRankingPresenter(model);
    }
}
