package com.bsm.mobile.domain.points.ranking;


import com.bsm.mobile.backend.score.points.IPointsService;
import com.bsm.mobile.backend.user.IUserRepository;
import com.bsm.mobile.domain.points.ranking.SideMissionRankingActivityMVP.Model;
import com.bsm.mobile.domain.points.ranking.SideMissionRankingActivityMVP.Presenter;

import dagger.Module;
import dagger.Provides;

@Module
public class SideMissionRankingActivityModule {

    @Provides
    public Model provideSideMissionRankingModel(IPointsService pointsService, IUserRepository userRepository){
        return new SideMissionRankingModel(pointsService, userRepository);
    }

    @Provides
    Presenter provideSideMissionRankingPresenter(Model model){
        return new SideMissionRankingPresenter(model);
    }
}
