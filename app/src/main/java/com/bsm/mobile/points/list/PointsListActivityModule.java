package com.bsm.mobile.points.list;

import dagger.Module;
import dagger.Provides;

import static com.bsm.mobile.points.list.PointsListActivityMVP.*;

@Module
public class PointsListActivityModule {

    @Provides
    public Presenter providePointsListPresenter(Model model){
        return new PointsListPresenter(model);
    }

    @Provides Model providePointsListModel(){
        return PointsListModel.builder()

                .build();
    }
}
