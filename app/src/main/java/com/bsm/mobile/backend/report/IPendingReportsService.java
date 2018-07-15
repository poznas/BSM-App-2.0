package com.bsm.mobile.backend.report;

import com.bsm.mobile.model.PendingReport;

import java.util.List;

import io.reactivex.Observable;

public interface IPendingReportsService {

    Observable<Long> getJudgePendingReportsNumber();

    Observable<Long> getProfessorPendingReportsNumber();

    /**
     * @param judgeId judge identifier
     * @return pending reports not rated by the judge yet
     */
    Observable<List<PendingReport>> getJudgePendingReports(String judgeId);

    /**
     * @return pending reports still requiring to be rated by a professor
     */
    Observable<List<PendingReport>> getProfessorPendingReports();
}
