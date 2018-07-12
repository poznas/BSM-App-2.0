package com.bsm.mobile.backend.report;

import com.bsm.mobile.model.PendingReport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public interface IPendingReportRepository {

    Observable<Map<String, PendingReport>> getPendingReports();
}
