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
    public IUserRepository provideUserRepository(IUserDetailsRepository detailsRepository){
        return new FirebaseUserRepository(detailsRepository);
    }

    @Singleton
    @Provides
    public IUserDetailsRepository provideUserDetailsRepository(){
        return new FirebaseUserDetailsRepository();
    }

    @Singleton
    @Provides
    public IUserPrivilegeRepository providePrivilegeRepository(){
        return new LocalUserPrivilegeRepository();
    }
}
