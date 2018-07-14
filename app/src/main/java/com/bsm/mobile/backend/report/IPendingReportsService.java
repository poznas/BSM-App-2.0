package com.bsm.mobile.backend.report;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface IPendingReportsService {

    Single<Long> getJudgePendingReportsNumber();

    Observable<Long> getProfessorPendingReportsNumber();
}
