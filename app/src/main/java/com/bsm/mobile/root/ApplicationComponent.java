package com.bsm.mobile.root;

import com.bsm.mobile.backend.BackendModule;
import com.bsm.mobile.backend.report.ReportBackendModule;
import com.bsm.mobile.backend.user.UserBackendModule;
import com.bsm.mobile.home.HomeActivity;
import com.bsm.mobile.home.HomeActivityModule;
import com.bsm.mobile.judge.list.JudgeSMListActivity;
import com.bsm.mobile.judge.list.JudgeSMListActivityModule;
import com.bsm.mobile.login.LoginActivity;
import com.bsm.mobile.login.LoginActivityModule;
import com.bsm.mobile.points.list.PointsListActivity;
import com.bsm.mobile.points.list.PointsListActivityModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        BackendModule.class,
        UserBackendModule.class,
        ReportBackendModule.class,
        LoginActivityModule.class,
        HomeActivityModule.class,
        JudgeSMListActivityModule.class,
        PointsListActivityModule.class
})
public interface ApplicationComponent {

    void inject(LoginActivity loginActivity);
    void inject(HomeActivity homeActivity);
    void inject(JudgeSMListActivity activity);
    void inject(PointsListActivity activity);
}
