package com.bsm.mobile.domain.login;

import com.bsm.mobile.common.MultiSubscriber;
import com.bsm.mobile.common.SnackMessage;
import com.bsm.mobile.common.Tagable;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import io.reactivex.Maybe;
import io.reactivex.Observable;

public interface LoginActivityMVP {

    interface View extends SnackMessage{

        void goHomeActivity();

        void showMessage(String message);

        void showProgress();

        void hideProgress();
    }

    interface Presenter extends Tagable, MultiSubscriber{

        void attachView(View view);

        void subscribeForAuth();

        void unsubscribe();
        
        void handleGoogleSignInResult(GoogleSignInResult result);
    }

    interface Model{

        Observable<Boolean> getSignInStatus();

        Maybe<Boolean> authWithGoogle(GoogleSignInAccount account);
    }
}
