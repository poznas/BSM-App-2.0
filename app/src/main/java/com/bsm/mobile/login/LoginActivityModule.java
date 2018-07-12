package com.bsm.mobile.login;

import dagger.Module;
import dagger.Provides;

import com.bsm.mobile.backend.user.IUserAuthService;
import com.bsm.mobile.login.LoginActivityMVP.*;

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
