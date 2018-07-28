package com.bsm.mobile.domain.points.list;

import com.bsm.mobile.backend.score.IScoreRepository;
import com.bsm.mobile.backend.score.points.IPointsService;
import com.bsm.mobile.backend.user.IUserAuthService;

import dagger.Module;
import dagger.Provides;

import static com.bsm.mobile.domain.points.list.PointsListActivityMVP.Model;
import static com.bsm.mobile.domain.points.list.PointsListActivityMVP.Presenter;

@Module
public class PointsListActivityModule {

    @Provides
    public Presenter providePointsListPresenter(Model model){
        return new PointsListPresenter(model);
    }

    @Provides Model providePointsListModel(IUserAuthService authService,
                                           IScoreRepository scoreRepository,
                                           IPointsService pointsService){
        return PointsListModel.builder()
                .userAuthService(authService)
                .scoreRepository(scoreRepository)
                .pointsService(pointsService)
                .build();
    }
}
