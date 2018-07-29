package com.bsm.mobile.domain.wizard.badge;

import com.bsm.mobile.common.MultiSubscriber;
import com.bsm.mobile.common.SnackMessage;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.domain.wizard.badge.model.BadgeInfoView;

import java.util.List;

import io.reactivex.Observable;

public interface BadgeInfoActivityMVP {

    interface View extends SnackMessage {

        void showProgress();

        void hideProgress();

        void updateBadgeInfoList(List<BadgeInfoView> list);
    }

    interface Presenter extends Tagable, MultiSubscriber {

        void attachView(View view);

        void unsubscribe();

        void subscribeForData();
    }

    interface Model{

        Observable<List<BadgeInfoView>> getBadgeInfoList();
    }
}
