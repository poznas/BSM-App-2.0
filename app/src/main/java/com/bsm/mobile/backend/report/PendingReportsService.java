package com.bsm.mobile.backend.report;

import android.support.annotation.NonNull;

import com.bsm.mobile.backend.user.IUserAuthService;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.legacy.model.PendingReport;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PendingReportsService implements IPendingReportsService, Tagable {

    private final IUserAuthService userAuthService;
    private final IPendingReportRepository pendingReportRepository;
    private final IReportRateRepository reportRateRepository;

    @Override
    public Observable<Long> getProfessorPendingReportsNumber() {

        return pendingReportRepository.getProfessorPendingReports()
                .map( pendingReports -> (long) pendingReports.size());
    }

    @Override
    public Observable<Long> getJudgePendingReportsNumber() {

        String userId = userAuthService.getCurrentUserId();

        return getJudgePendingReports(userId)
                .map( pendingReports -> (long) pendingReports.size());
    }

    /**
     * @return pending reports still requiring to be rated by a professor
     */
    @Override
    public Observable<List<PendingReport>> getProfessorPendingReports() {
        return pendingReportRepository.getProfessorPendingReports()
                .map(this::convertPendingReportsMapToList);
    }

    /**
     * @param currentJudgeId current judge identifier
     * @return pending reports not rated by the judge yet
     */
    @Override
    public Observable<List<PendingReport>> getJudgePendingReports(String currentJudgeId) {

        return pendingReportRepository.getJudgePendingReports().observeOn(Schedulers.io())
                .map(this::convertPendingReportsMapToList)
                .map(pendingReports -> {
                    List<PendingReport> resultList = new LinkedList<>();

                    for( PendingReport report : pendingReports ){
                        if(
                                !reportRateRepository
                                        .getReportJudgeRatersIds(report.getRpid())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(Schedulers.io())
                                        .map(judgeIds -> judgeIds.contains(currentJudgeId))
                                        .blockingFirst()
                                )
                        {
                            resultList.add(report);
                        }
                    }
                    return resultList;
                });
    }


    @NonNull
    private List<PendingReport> convertPendingReportsMapToList(Map<String, PendingReport> map) {
        List<PendingReport> list = new LinkedList<>();
        for( String reportId : map.keySet() ){
            list.add(map.get(reportId));
        }
        return list;
    }
}
