package com.bsm.mobile.backend.user;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UserBackendModule {

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
    public IUserPrivilegeRepository providePrivilegeRepository(){
        return new LocalUserPrivilegeRepository();
    }
}
