package com.bsm.mobile.domain.home;


import android.content.Context;
import android.util.Log;

import com.bsm.mobile.common.utils.UserDataValidator;
import com.bsm.mobile.legacy.model.Privilege;
import com.bsm.mobile.legacy.model.User;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;

import static com.bsm.mobile.common.resource.Constants.BRAND_JUDGE;
import static com.bsm.mobile.common.resource.Constants.BRAND_PROF_RATE;
import static com.bsm.mobile.common.resource.Constants.LABEL_JUDGE;
import static com.bsm.mobile.common.resource.Constants.LABEL_PROFESSOR;
import static com.bsm.mobile.common.resource.Message.AUTH_STATE_SIGNED_IN;
import static com.bsm.mobile.common.resource.Message.AUTH_STATE_SIGNED_OUT;
import static com.bsm.mobile.domain.home.HomeActivityMVP.Model;
import static com.bsm.mobile.domain.home.HomeActivityMVP.Presenter;
import static com.bsm.mobile.domain.home.HomeActivityMVP.View;

@RequiredArgsConstructor
public class HomePresenter implements Presenter {

    private View view;
    private final Model model;

    private CompositeDisposable subscriptions;
    private List<Privilege> privileges;

    @Override
    public void attachView(View view) {
        this.view = view;
        privileges = new LinkedList<>();
        subscriptions = new CompositeDisposable();
        model.createGoogleApiClient((Context) view);
        model.attachRoom((Context) view);
        this.view.showProgress();
    }

    @Override
    public void unsubscribe() {
        clearSubscriptions(subscriptions);
        model.disconnectGoogleApi();
    }

    @Override
    public void subscribeForData() {
        subscribeForAuth();
        subscribeForUserData();
        subscribeForScores();
        model.connectGoogleApi();
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

        subscriptions.add(
                model.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(user -> view.hideProgress())
                .subscribe(user -> {
                        Log.d(getTag(), "user data: " + user);

                        if( userIsAuthorized(user.getLabel())){
                            subscribeForPrivileges(user);

                            view.setTeamImagesClickListeners();
                            if( user.getLabel().equals(LABEL_JUDGE)){
                                subscribeForJudgePendingReports();
                                model.makeDeviceSubscribeForJudgeNotifications();
                                model.deleteNotifications((Context) view);
                            }else {
                                model.makeDeviceUnsubscribeFromJudgeNotifications();
                                if( user.getLabel().equals(LABEL_PROFESSOR)){
                                    subscribeForProfessorPendingReports();
                                }
                            }
                        }else {
                            view.updatePrivileges(Collections.emptyList());
                        }
                    },error -> view.showMessage(error.getMessage())
                )
        );
    }

    private void subscribeForPrivileges(User user) {
        subscriptions.add(
                model.getUserPrivileges(user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(privileges -> {
                        this.privileges.clear();
                        this.privileges.addAll(privileges);
                        view.updatePrivileges(privileges);
                    })
        );
    }

    private void subscribeForJudgePendingReports() {
        subscriptions.add(
                model.getJudgePendingReportsNumber()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pendingReportsNumber ->
                            updatePrivileges(BRAND_JUDGE, pendingReportsNumber)
                    )
        );
    }

    private void subscribeForProfessorPendingReports() {
        subscriptions.add(
                model.getProfessorPendingReportsNumber()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(pendingReportsNumber ->
                                updatePrivileges(BRAND_PROF_RATE, pendingReportsNumber)
                        )
        );
    }

    private void updatePrivileges(String brand, Long pendingReportsNumber) {
        if(privileges == null) return;

        for(Privilege privilege : privileges){
            if(privilege.getBrand().equals(brand)){
                privilege.setPendingReports(pendingReportsNumber.intValue());
            }
        }
        view.updatePrivileges(privileges);
    }

    @Override
    public boolean signOut() {
        model.signOut();
        view.goLoginActivity();
        return true;
    }

    private boolean userIsAuthorized(String userLabel) {
        boolean authorized = UserDataValidator.validLabel(userLabel);
        view.showUnauthorizedMessage(!authorized);
        return authorized;
    }
}
