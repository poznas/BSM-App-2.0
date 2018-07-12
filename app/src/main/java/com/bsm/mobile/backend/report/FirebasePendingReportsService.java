package com.bsm.mobile.backend.report;

import com.bsm.mobile.backend.user.IUserAuthService;

import java.util.Map;

import io.reactivex.Single;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FirebasePendingReportsService implements IPendingReportsService {

    private final IUserAuthService userAuthService;
    private final IPendingReportRepository pendingReportRepository;
    private final IReportRateRepository reportRateRepository;

    @Override
    public Single<Long> getJudgePendingReportsNumber() {

        String userId = userAuthService.getCurrentUserId();

        return pendingReportRepository.getPendingReports()
                .map(Map::keySet)
                .flatMapIterable(reportIds -> reportIds)
                .filter(reportId ->
                        reportRateRepository
                                .getReportJudgeRatersIds(reportId)
                                .flatMapIterable(judgeIds -> judgeIds)
                                .filter(judgeId -> judgeId.equals(userId))
                                .count().blockingGet() == 0
                )
                .count();
    }

    @Override
    public Single<Long> getProfessorPendingReportsNumber() {
        //TODO:
        return null;
    }
}
