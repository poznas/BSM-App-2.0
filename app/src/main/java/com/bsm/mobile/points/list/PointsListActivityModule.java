package com.bsm.mobile.points.list;

import com.bsm.mobile.backend.score.IScoreRepository;
import com.bsm.mobile.backend.score.points.IPointsService;
import com.bsm.mobile.backend.user.IUserAuthService;

import dagger.Module;
import dagger.Provides;

import static com.bsm.mobile.points.list.PointsListActivityMVP.*;

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
