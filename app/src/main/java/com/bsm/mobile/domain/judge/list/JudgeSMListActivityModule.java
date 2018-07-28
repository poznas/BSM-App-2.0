package com.bsm.mobile.domain.judge.list;

import com.bsm.mobile.backend.report.IPendingReportsService;
import com.bsm.mobile.backend.user.IUserAuthService;

import dagger.Module;
import dagger.Provides;

import static com.bsm.mobile.domain.judge.list.JudgeSMListActivityMVP.Model;
import static com.bsm.mobile.domain.judge.list.JudgeSMListActivityMVP.Presenter;

@Module
public class JudgeSMListActivityModule  {

    @Provides
    public Presenter provideJudgeSMListPresenter(Model model){
        return new JudgeSMListPresenter(model);
    }

    @Provides
    public Model provideJudgeSMListModel(IUserAuthService authService,
                                         IPendingReportsService pendingReportsService){
        return new JudgeSMListModel(authService, pendingReportsService);
    }

}
