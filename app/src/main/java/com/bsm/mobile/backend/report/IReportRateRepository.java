package com.bsm.mobile.backend.report;


import java.util.Set;

import io.reactivex.Observable;

public interface IReportRateRepository {

    Observable<Set<String>> getReportRatersIds(String reportId);
}
