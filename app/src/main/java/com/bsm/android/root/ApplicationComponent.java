package com.bsm.android.root;

import com.bsm.android.backend.BackendModule;
import com.bsm.android.home.HomeActivity;
import com.bsm.android.home.HomeActivityModule;
import com.bsm.android.login.LoginActivity;
import com.bsm.android.login.LoginActivityModule;

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
