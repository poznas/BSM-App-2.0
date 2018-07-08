package com.bsm.android.login;

import com.bsm.android.core.Tagable;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.AuthResult;

import io.reactivex.Observable;

public interface LoginActivityMVP {

    interface View{

        void goHomeActivity();

        void showSnackBarMessage(String message);

        void showProgress();

        void hideProgress();
    }

    interface Presenter extends Tagable{

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
