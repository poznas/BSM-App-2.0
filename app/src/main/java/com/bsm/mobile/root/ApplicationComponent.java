package com.bsm.mobile.root;

import com.bsm.mobile.backend.BackendModule;
import com.bsm.mobile.backend.report.ReportBackendModule;
import com.bsm.mobile.backend.score.ScoreBackendModule;
import com.bsm.mobile.backend.user.UserBackendModule;
import com.bsm.mobile.domain.home.HomeActivity;
import com.bsm.mobile.domain.home.HomeActivityModule;
import com.bsm.mobile.domain.judge.list.JudgeSMListActivity;
import com.bsm.mobile.domain.judge.list.JudgeSMListActivityModule;
import com.bsm.mobile.domain.login.LoginActivity;
import com.bsm.mobile.domain.login.LoginActivityModule;
import com.bsm.mobile.domain.points.list.PointsListActivity;
import com.bsm.mobile.domain.points.list.PointsListActivityModule;
import com.bsm.mobile.domain.professor.admin.AdminActivity;
import com.bsm.mobile.domain.professor.admin.AdminActivityModule;
import com.bsm.mobile.domain.professor.admin.user.EditUserActivity;
import com.bsm.mobile.domain.professor.admin.user.EditUserActivityModule;
import com.bsm.mobile.domain.professor.sm.list.RateSMListActivity;
import com.bsm.mobile.domain.professor.sm.list.RateSMListActivityModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        BackendModule.class,
        UserBackendModule.class,
        ReportBackendModule.class,
        ScoreBackendModule.class,
        LoginActivityModule.class,
        HomeActivityModule.class,
        JudgeSMListActivityModule.class,
        PointsListActivityModule.class,
        RateSMListActivityModule.class,
        AdminActivityModule.class,
        EditUserActivityModule.class
})
public interface ApplicationComponent {

    void inject(LoginActivity loginActivity);
    void inject(HomeActivity homeActivity);
    void inject(JudgeSMListActivity activity);
    void inject(PointsListActivity activity);
    void inject(RateSMListActivity activity);
    void inject(AdminActivity activity);
    void inject(EditUserActivity activity);
}
