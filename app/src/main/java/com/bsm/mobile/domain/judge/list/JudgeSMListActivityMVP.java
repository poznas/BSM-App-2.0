package com.bsm.mobile.domain.judge.list;

import com.bsm.mobile.common.MultiSubscriber;
import com.bsm.mobile.common.SnackMessage;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.legacy.model.PendingReport;

import java.util.List;

import io.reactivex.Observable;

/**
 * SM - Side Mission
 * Activity displays list of recently reported SM's which:
 *  -   still require more rates
 *  -   are not rated by current user (judge)
 */
public interface JudgeSMListActivityMVP {

    interface View extends SnackMessage {

        void showProgress();

        void hideProgress();

        void updatePendingReports(List<PendingReport> reports);
    }

    interface Presenter extends Tagable, MultiSubscriber {

        void attachView(View view);

        void unsubscribe();

        void subscribeForData();
    }

    interface Model{

        Observable<List<PendingReport>> getPendingReports();
    }
}
