package com.bsm.mobile.root;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lombok.AllArgsConstructor;

@Module
@AllArgsConstructor
public class ApplicationModule {

    private Application application;

    @Provides
    @Singleton
    public Context provideContext(){
        return application;
    }
}





