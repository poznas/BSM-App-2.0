package com.bsm.mobile.backend;

import com.bsm.mobile.backend.notifications.FirebaseNotificationService;
import com.bsm.mobile.backend.notifications.INotificationService;
import com.bsm.mobile.backend.score.FirebaseScoreRepository;
import com.bsm.mobile.backend.score.IScoreRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * For backend modules with only one component
 */

@Module
public class BackendModule {

    @Singleton
    @Provides
    public INotificationService provideNotificationService(){
        return new FirebaseNotificationService();
    }

    @Singleton
    @Provides
    public IScoreRepository provideScoreRepository(){
        return new FirebaseScoreRepository();
    }

}
