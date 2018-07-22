package com.bsm.mobile.professor.sm.list;

import com.bsm.mobile.backend.report.IPendingReportsService;
import com.bsm.mobile.professor.sm.list.RateSMListActivityMVP.Model;
import com.bsm.mobile.professor.sm.list.RateSMListActivityMVP.Presenter;

import dagger.Module;
import dagger.Provides;

@Module
public class RateSMListActivityModule {

    @Provides
    public Presenter provideRateSMListPresenter(Model model){
        return new RateSMListPresenter(model);
    }

    @Provides
    public Model provideRateSMListModel(IPendingReportsService pendingReportsService){
        return new RateSMListModel(pendingReportsService);
    }
}
