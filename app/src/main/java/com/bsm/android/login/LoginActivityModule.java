package com.bsm.android.login;

import dagger.Module;
import dagger.Provides;

import com.bsm.android.backend.user.IUserAuthService;
import com.bsm.android.login.LoginActivityMVP.*;

@Module
public class LoginActivityModule {

    @Provides
    public Presenter provideLoginPresenter(Model model){
        return new LoginPresenter(model);
    }

    @Provides
    public Model provideLoginModel(IUserAuthService repository){
        return new LoginModel(repository);
    }
}
