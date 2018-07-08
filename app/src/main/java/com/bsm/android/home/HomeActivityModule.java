package com.bsm.android.home;

import dagger.Module;
import dagger.Provides;

import static com.bsm.android.home.HomeActivityMVP.*;

@Module
public class HomeActivityModule {

    @Provides
    public Presenter provideHomePresenter(Model model){
        return new HomePresenter(model);
    }

    @Provides
    public Model provideHomeModel(){
        return new HomeModel();
    }
}
