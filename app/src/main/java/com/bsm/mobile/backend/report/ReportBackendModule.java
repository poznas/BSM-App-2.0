package com.bsm.mobile.backend.report;

import com.bsm.mobile.backend.user.IUserAuthService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ReportBackendModule {

    @Singleton
    @Provides
    public IReportRepository provideReportRepository(){
        return new FirebaseReportRepository();
    }

    @Singleton
    @Provides
    public IReportRateRepository provideReportRateRepository(){
        return new FirebaseReportRateRepository();
    }

    @Singleton
    @Provides
    public IPendingReportRepository providePendingReportRepository(){
        return new FirebasePendingReportRepository();
    }

    @Singleton
    @Provides
    public IPendingReportsService providePendingReportsService(
            IUserAuthService authService,
            IReportRateRepository reportRateRepository,
            IPendingReportRepository pendingReportRepository){

        return new PendingReportsService(
                authService,
                pendingReportRepository,
                reportRateRepository
        );
    }
}
