package com.bsm.mobile.backend.report;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface IReportRepository {

    Single<Boolean> setReportLockState(boolean unlocked);

    Observable<Boolean> getReportLockState();

}
