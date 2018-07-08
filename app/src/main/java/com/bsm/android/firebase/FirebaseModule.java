package com.bsm.android.firebase;

import com.bsm.android.firebase.user.FirebaseUserAuthService;
import com.bsm.android.firebase.user.FirebaseUserRepository;
import com.bsm.android.firebase.user.IUserAuthService;
import com.bsm.android.firebase.user.IUserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {

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
}
