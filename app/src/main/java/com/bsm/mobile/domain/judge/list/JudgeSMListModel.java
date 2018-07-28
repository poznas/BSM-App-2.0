package com.bsm.mobile.domain.judge.list;

import com.bsm.mobile.backend.report.IPendingReportsService;
import com.bsm.mobile.backend.user.IUserAuthService;
import com.bsm.mobile.legacy.model.PendingReport;

import java.util.List;

import io.reactivex.Observable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static com.bsm.mobile.domain.judge.list.JudgeSMListActivityMVP.Model;

@Data
@Builder
@AllArgsConstructor
public class JudgeSMListModel implements Model {

    private IUserAuthService userAuthService;
    private IPendingReportsService pendingReportsService;

    @Override
    public Observable<List<PendingReport>> getPendingReports() {
        return pendingReportsService.getJudgePendingReports(userAuthService.getCurrentUserId());
    }
}
