package com.bsm.mobile.backend.report;

import com.bsm.mobile.legacy.model.PendingReport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public interface IPendingReportRepository {

    /**
     * @return all pending reports still requiring more judge's rates
     */
    Observable<Map<String, PendingReport>> getJudgePendingReports();

    Observable<Map<String, PendingReport>> getProfessorPendingReports();

}
