package com.bsm.android.home;

import com.bsm.android.core.MultiSubscriber;
import com.bsm.android.core.SnackMessage;
import com.bsm.android.core.Tagable;
import com.bsm.android.model.Privilege;
import com.bsm.android.model.User;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;


public interface HomeActivityMVP {

    interface View extends SnackMessage{

        void showProgress();

        void hideProgress();
        
        void showMessage(String message);

        void goLoginActivity();

        void signOutFromGoogle();

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

        Observable<Boolean> getSignInStatus();

        Observable<User> getUser();

        void makeDeviceSubscribeForJudgeNotifications();

        void makeDeviceUnsubscribeFromJudgeNotifications();

        Observable<List<Privilege>> getUserPrivileges(User user);
        
        Observable<HashMap<String, Long>> getScores();
    }
}
