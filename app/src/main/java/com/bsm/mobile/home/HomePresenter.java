package com.bsm.mobile.home;


import android.util.Log;

import com.bsm.mobile.model.User;

import java.util.LinkedList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.bsm.mobile.Constants.*;
import static com.bsm.mobile.Message.*;
import static com.bsm.mobile.home.HomeActivityMVP.*;

public class HomePresenter implements Presenter {

    private View view;
    private Model model;

    private LinkedList<Disposable> subscriptions;

    public HomePresenter(Model model) {
        this.model = model;
    }

    @Override
    public void attachView(View view) {
        this.view = view;
        subscriptions = new LinkedList<>();
    }

    @Override
    public void unsubscribe() {
        clearSubscriptions(subscriptions);
    }

    @Override
    public void subscribeForData() {
        subscribeForAuth();
        subscribeForUserData();
        subscribeForScores();
    }

    private void subscribeForAuth() {
        Disposable authSubscription = model.getSignInStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isSignedIn -> {
                    if(isSignedIn){
                        Log.i(getTag(), AUTH_STATE_SIGNED_IN);
                    }else {
                        Log.i(getTag(), AUTH_STATE_SIGNED_OUT);
                        if(view != null){
                            view.goLoginActivity();
                        }
                    }
                });
        subscriptions.add(authSubscription);
    }

    private void subscribeForScores() {
        Disposable scoreSubscription = model.getScores()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::updateScores);

        subscriptions.add(scoreSubscription);
    }

    private void subscribeForUserData() {
        view.showProgress();

        Disposable userSubscription = model.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(view::hideProgress)
                .subscribe(user -> {
                    Log.d(getTag(), "user data: " + user);
                    if( user != null && user.getLabel() != null ){

                        subscribeForPrivileges(user);
                        if( userIsAuthorized(user.getLabel())){
                            view.setTeamImagesClickListeners();
                        }

                        if( user.getLabel().equals(LABEL_JUDGE)){
                            subscribeForJudgePendingReports(user);
                            model.makeDeviceSubscribeForJudgeNotifications();
                        }else {
                            model.makeDeviceUnsubscribeFromJudgeNotifications();
                            if( user.getLabel().equals(LABEL_PROFESSOR)){
                                subscribeForProfessorPendingReports(user);
                            }
                        }
                    }
                }, error -> {
                    view.showMessage(error.getMessage());
                });
        subscriptions.add(userSubscription);
    }

    private void subscribeForPrivileges(User user) {
        view.showProgress();

        Disposable privilegesSubscription = model.getUserPrivileges(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(view::hideProgress)
                .subscribe(view::updatePrivileges);

        subscriptions.add(privilegesSubscription);
    }

    private void subscribeForJudgePendingReports(User user) {
        subscriptions.add(
                model.getJudgePendingReportsNumber()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            //TODO:
                    )
        );
    }

    private void subscribeForProfessorPendingReports(User user) {
        //TODO:
    }

    @Override
    public boolean signOut() {
        model.signOut();
        view.signOutFromGoogle();
        view.goLoginActivity();
        return true;
    }

    private boolean userIsAuthorized(String userLabel) {
        return userLabel.equals(LABEL_WIZARD)
                || userLabel.equals(LABEL_PROFESSOR)
                || userLabel.equals(LABEL_JUDGE);
    }
}
