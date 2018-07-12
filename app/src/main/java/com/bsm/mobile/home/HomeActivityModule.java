package com.bsm.mobile.home;

import com.bsm.mobile.backend.notifications.INotificationService;
import com.bsm.mobile.backend.score.IScoreRepository;
import com.bsm.mobile.backend.user.IUserAuthService;
import com.bsm.mobile.backend.user.IUserPrivilegeRepository;
import com.bsm.mobile.backend.user.IUserRepository;

import dagger.Module;
import dagger.Provides;

import static com.bsm.mobile.home.HomeActivityMVP.*;

@Module
public class HomeActivityModule {

    @Provides
    public Presenter provideHomePresenter(Model model){
        return new HomePresenter(model);
    }

    @Provides
    public Model provideHomeModel(IUserAuthService authService,
                                  IUserRepository repository,
                                  INotificationService notificationService,
                                  IUserPrivilegeRepository privilegeRepository,
                                  IScoreRepository scoreRepository){

        return new HomeModel(authService, repository,
                notificationService, privilegeRepository, scoreRepository);
    }
}
