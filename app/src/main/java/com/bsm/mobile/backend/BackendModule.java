package com.bsm.mobile.backend;

import com.bsm.mobile.backend.notifications.FirebaseNotificationService;
import com.bsm.mobile.backend.notifications.INotificationService;
import com.bsm.mobile.backend.score.FirebaseScoreRepository;
import com.bsm.mobile.backend.score.IScoreRepository;
import com.bsm.mobile.backend.user.FirebaseUserAuthService;
import com.bsm.mobile.backend.user.FirebaseUserRepository;
import com.bsm.mobile.backend.user.IUserPrivilegeRepository;
import com.bsm.mobile.backend.user.IUserAuthService;
import com.bsm.mobile.backend.user.IUserRepository;
import com.bsm.mobile.backend.user.LocalUserPrivilegeRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BackendModule {

    @Singleton
    @Provides
    public IUserAuthService provideFirebaseAuthRepository(IUserRepository repository){
        return new FirebaseUserAuthService(repository);
    }

    @Singleton
    @Provides
    public IUserRepository provideUserRepository(){
        return new FirebaseUserRepository();
    }

    @Singleton
    @Provides
    public INotificationService provideNotificationService(){
        return new FirebaseNotificationService();
    }

    @Singleton
    @Provides
    public IUserPrivilegeRepository providePrivilegeRepository(){
        return new LocalUserPrivilegeRepository();
    }

    @Singleton
    @Provides
    public IScoreRepository provideScoreRepository(){
        return new FirebaseScoreRepository();
    }
}
