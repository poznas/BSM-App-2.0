package com.bsm.mobile.points.list;

import android.os.Bundle;

import com.bsm.mobile.common.MultiSubscriber;
import com.bsm.mobile.common.SnackMessage;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.legacy.model.PointsInfo;

import java.util.List;

import io.reactivex.Observable;

public interface PointsListActivityMVP {

    interface View extends SnackMessage {

        void showProgress();

        void hideProgress();

        void updatePointsRecords(List<PointsInfo> records);

        void setTeamDisplay(String teamId);

        void updateScore(long score);

        void setPointsInvalidationPermission(boolean permission);
    }

    interface Presenter extends Tagable, MultiSubscriber {

        void attachView(View view);

        void handleBundle(Bundle bundle);

        void unsubscribe();

        void subscribeForData();

        void invalidatePoints(PointsInfo points);
    }

    interface Model{

        Observable<List<PointsInfo>> getPointsRecords(String teamId);

        Observable<Long> getScore(String teamId);

        Observable<String> getUserLabel();

        void invalidatePoints(PointsInfo points);
    }
}
