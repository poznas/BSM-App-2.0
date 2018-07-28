package com.bsm.mobile.domain.login;

import com.bsm.mobile.backend.user.IUserAuthService;
import com.bsm.mobile.domain.login.LoginActivityMVP.Model;
import com.bsm.mobile.domain.login.LoginActivityMVP.Presenter;

import dagger.Module;
import dagger.Provides;

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
