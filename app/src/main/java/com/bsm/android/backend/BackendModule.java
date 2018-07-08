package com.bsm.android.backend;

import com.bsm.android.backend.notifications.FirebaseNotificationService;
import com.bsm.android.backend.notifications.INotificationService;
import com.bsm.android.backend.score.FirebaseScoreRepository;
import com.bsm.android.backend.score.IScoreRepository;
import com.bsm.android.backend.user.FirebaseUserAuthService;
import com.bsm.android.backend.user.FirebaseUserRepository;
import com.bsm.android.backend.user.IUserPrivilegeRepository;
import com.bsm.android.backend.user.IUserAuthService;
import com.bsm.android.backend.user.IUserRepository;
import com.bsm.android.backend.user.LocalUserPrivilegeRepository;

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
