package com.bsm.android.login;


import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import java.util.LinkedList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.bsm.android.login.LoginActivityMVP.*;


public class LoginPresenter implements Presenter {

    private View view;
    private Model model;

    private LinkedList<Disposable> subscriptions;

    public LoginPresenter(Model model) {
        this.model = model;
    }

    @Override
    public void attachView(View view) {
        this.view = view;
        subscriptions = new LinkedList<>();
    }

    @Override
    public void subscribeForAuth() {
        Disposable authSubscription = model.getSignInStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isSignedIn -> {
                    if(isSignedIn && view != null){
                        Log.i(getTag(), "auth state - signed in");
                        view.goHomeActivity();
                    }else {
                        Log.i(getTag(), "auth state - signed out");
                    }
                });
        subscriptions.add(authSubscription);
    }

    @Override
    public void unsubscribe() {
        clearSubscriptions(subscriptions);
    }

    @Override
    public void handleGoogleSignInResult(GoogleSignInResult result) {
        view.hideProgress();
        if(result.isSuccess()){
            // Google sign in was successful, now auth with firebase
            view.showProgress();
            GoogleSignInAccount account = result.getSignInAccount();

            Disposable subscription = model.authWithGoogle(account)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnTerminate(view::hideProgress)
                    .subscribe(
                            authResult ->
                                    subscribeForAuth(),
                            error ->
                                    view.showMessage(error.getLocalizedMessage())
                    );
            subscriptions.add(subscription);
        }else {
            view.showMessage(result.getStatus().getStatusMessage());
        }
    }
}
