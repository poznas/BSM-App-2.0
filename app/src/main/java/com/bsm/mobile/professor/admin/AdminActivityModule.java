package com.bsm.mobile.professor.admin;

import com.bsm.mobile.backend.report.IReportRepository;
import com.bsm.mobile.backend.user.IUserRepository;
import com.bsm.mobile.professor.admin.AdminActivityMVP.Model;
import com.bsm.mobile.professor.admin.AdminActivityMVP.Presenter;

import dagger.Module;
import dagger.Provides;

@Module
public class AdminActivityModule {

    @Provides
    public Model provideAdminModel(IUserRepository userRepository, IReportRepository reportRepository){
        return new AdminModel(reportRepository, userRepository);
    }

    @Provides
    public Presenter provideAdminPresenter(Model model){
        return new AdminPresenter(model);
    }
}
