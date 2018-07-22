package com.bsm.mobile.login;


import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import java.util.LinkedList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.bsm.mobile.Message.AUTH_STATE_SIGNED_IN;
import static com.bsm.mobile.Message.AUTH_STATE_SIGNED_OUT;
import static com.bsm.mobile.Message.SIGN_IN_WITH_FAILURE;
import static com.bsm.mobile.login.LoginActivityMVP.Model;
import static com.bsm.mobile.login.LoginActivityMVP.Presenter;
import static com.bsm.mobile.login.LoginActivityMVP.View;

@Slf4j
@RequiredArgsConstructor
public class LoginPresenter implements Presenter {



    private View view;
    private final Model model;

    private LinkedList<Disposable> subscriptions;

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
                        Log.i(getTag(), AUTH_STATE_SIGNED_IN);
                        view.goHomeActivity();
                    }else {
                        Log.i(getTag(), AUTH_STATE_SIGNED_OUT);
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

            subscriptions.add(
              model.authWithGoogle(account)
                      .subscribeOn(Schedulers.io())
                      .observeOn(AndroidSchedulers.mainThread())
                      .doAfterTerminate(view::hideProgress)
                      .subscribe(
                              signInSuccess -> {
                                  if (signInSuccess){
                                      subscribeForAuth();
                                  }else {
                                      view.showMessage(SIGN_IN_WITH_FAILURE);
                                  }
                              },
                              error -> view.showMessage(error.getMessage())
                      )
            );
            /*
            Disposable subscription = model.authWithGoogle(account)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnTerminate(view::hideProgress)
                    .subscribe(
                            signInSuccess -> {
                                if (signInSuccess){
                                    subscribeForAuth();
                                }else {
                                    view.showMessage(SIGN_IN_WITH_FAILURE);
                                }
                            },
                            error -> view.showMessage(error.getMessage())
                    );
            subscriptions.add(subscription);
            */
        }else {
            view.showMessage(result.getStatus().getStatusMessage());
        }
    }
}
