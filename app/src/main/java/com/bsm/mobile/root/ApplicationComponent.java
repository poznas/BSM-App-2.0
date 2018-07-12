package com.bsm.mobile.root;

import com.bsm.mobile.backend.BackendModule;
import com.bsm.mobile.home.HomeActivity;
import com.bsm.mobile.home.HomeActivityModule;
import com.bsm.mobile.login.LoginActivity;
import com.bsm.mobile.login.LoginActivityModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        BackendModule.class,
        LoginActivityModule.class,
        HomeActivityModule.class
})
public interface ApplicationComponent {


    void inject(LoginActivity loginActivity);
    void inject(HomeActivity homeActivity);
}
