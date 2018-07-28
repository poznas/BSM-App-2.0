package com.bsm.mobile.domain.professor.admin.user;

import com.bsm.mobile.backend.user.IUserRepository;
import com.bsm.mobile.domain.professor.admin.user.EditUserActivityMVP.Model;
import com.bsm.mobile.domain.professor.admin.user.EditUserActivityMVP.Presenter;

import dagger.Module;
import dagger.Provides;

@Module
public class EditUserActivityModule {

    @Provides
    public Model provideEditUserModel(IUserRepository userRepository){
        return new EditUserModel(userRepository);
    }

    @Provides
    public Presenter provideEditUserPresenter(Model model){
        return new EditUserPresenter(model);
    }
}
