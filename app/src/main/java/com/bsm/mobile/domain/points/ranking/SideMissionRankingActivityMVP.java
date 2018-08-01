package com.bsm.mobile.domain.points.ranking;

import com.bsm.mobile.common.MultiSubscriber;
import com.bsm.mobile.common.SnackMessage;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.legacy.model.PointsInfo;

import java.util.List;

import io.reactivex.Observable;

public interface SideMissionRankingActivityMVP {

    interface View extends SnackMessage {

        void showProgress();

        void hideProgress();

        void updateRankingList(List<PointsInfo> list);
    }

    interface Presenter extends Tagable, MultiSubscriber {

        void attachView(View view);

        void unsubscribe();

        void subscribeForData();
    }

    interface Model{

        Observable<List<PointsInfo>> getRanking();
    }
}
