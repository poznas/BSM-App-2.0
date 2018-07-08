package com.bsm.android.home;

import com.bsm.android.firebase.notifications.INotificationService;
import com.bsm.android.firebase.user.IUserAuthService;
import com.bsm.android.firebase.user.IUserRepository;

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
    public Model provideHomeModel(IUserAuthService authService,
                                  IUserRepository repository,
                                  INotificationService notificationService){

        return new HomeModel(authService, repository, notificationService);
    }
}
