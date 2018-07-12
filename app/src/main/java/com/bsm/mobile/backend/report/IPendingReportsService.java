package com.bsm.mobile.backend.report;

import com.bsm.mobile.model.User;

import io.reactivex.Single;

public interface IPendingReportsService {

    Single<Long> getJudgePendingReportsNumber();

    Single<Long> getProfessorPendingReportsNumber();
}
