package com.bsm.android.root;

import com.bsm.android.firebase.FirebaseModule;
import com.bsm.android.home.HomeActivity;
import com.bsm.android.home.HomeActivityModule;
import com.bsm.android.login.LoginActivity;
import com.bsm.android.login.LoginActivityModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        FirebaseModule.class,
        LoginActivityModule.class,
        HomeActivityModule.class
})
public interface ApplicationComponent {


    void inject(LoginActivity loginActivity);
    void inject(HomeActivity homeActivity);
}
