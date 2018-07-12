package com.bsm.mobile.login;

import com.bsm.mobile.core.MultiSubscriber;
import com.bsm.mobile.core.SnackMessage;
import com.bsm.mobile.core.Tagable;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.AuthResult;

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

        Observable<AuthResult> authWithGoogle(GoogleSignInAccount account);
    }
}
