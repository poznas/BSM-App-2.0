package com.bsm.mobile.home;

import com.bsm.mobile.common.MultiSubscriber;
import com.bsm.mobile.common.SnackMessage;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.legacy.model.Privilege;
import com.bsm.mobile.legacy.model.User;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;


public interface HomeActivityMVP {

    interface View extends SnackMessage{

        void showProgress();

        void hideProgress();
        
        void showMessage(String message);

        void goLoginActivity();

        void updatePrivileges(List<Privilege> privileges);

        void setTeamImagesClickListeners();

        void updateScores(HashMap<String, Long> scores);
    }

    interface Presenter extends Tagable, MultiSubscriber {

        void attachView(View view);

        void unsubscribe();

        void subscribeForData();

        boolean signOut();
    }

    interface Model{

        void signOut();

        void connectGoogleApi();

        void disconnectGoogleApi();

        Observable<Boolean> getSignInStatus();

        Observable<User> getUser();

        void makeDeviceSubscribeForJudgeNotifications();

        void makeDeviceUnsubscribeFromJudgeNotifications();

        Observable<List<Privilege>> getUserPrivileges(User user);
        
        Observable<HashMap<String, Long>> getScores();

        Observable<Long> getJudgePendingReportsNumber();

        Observable<Long> getProfessorPendingReportsNumber();

        void createGoogleApiClient(View view);
    }
}
