package com.bsm.mobile.professor.sm.list;

import com.bsm.mobile.common.MultiSubscriber;
import com.bsm.mobile.common.SnackMessage;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.legacy.model.PendingReport;

import java.util.List;

import io.reactivex.Observable;

public interface RateSMListActivityMVP {

    interface View extends SnackMessage {

        void showProgress();

        void hideProgress();

        void updateRequireProfessorRateReports(List<PendingReport> reports);
    }

    interface Presenter extends Tagable, MultiSubscriber {

        void attachView(View view);

        void unsubscribe();

        void subscribeForData();
    }

    interface Model{

        Observable<List<PendingReport>> getRequireProfessorRateReports();
    }
}
