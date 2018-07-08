package com.bsm.android.home;

import com.bsm.android.backend.notifications.INotificationService;
import com.bsm.android.backend.score.IScoreRepository;
import com.bsm.android.backend.user.IUserAuthService;
import com.bsm.android.backend.user.IUserPrivilegeRepository;
import com.bsm.android.backend.user.IUserRepository;

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
                                  INotificationService notificationService,
                                  IUserPrivilegeRepository privilegeRepository,
                                  IScoreRepository scoreRepository){

        return new HomeModel(authService, repository,
                notificationService, privilegeRepository, scoreRepository);
    }
}
