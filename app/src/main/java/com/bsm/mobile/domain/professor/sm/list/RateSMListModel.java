package com.bsm.mobile.professor.sm.list;

import com.bsm.mobile.backend.report.IPendingReportsService;
import com.bsm.mobile.legacy.model.PendingReport;

import java.util.List;

import io.reactivex.Observable;
import lombok.AllArgsConstructor;

import static com.bsm.mobile.professor.sm.list.RateSMListActivityMVP.Model;

@AllArgsConstructor
public class RateSMListModel implements Model {

    private IPendingReportsService pendingReportsService;

    @Override
    public Observable<List<PendingReport>> getRequireProfessorRateReports() {
        return pendingReportsService.getProfessorPendingReports();
    }
}
