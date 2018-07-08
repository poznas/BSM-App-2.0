package com.bsm.android.home;


import java.util.LinkedList;

import io.reactivex.disposables.Disposable;

import static com.bsm.android.home.HomeActivityMVP.*;

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

    }

    @Override
    public void subscribeForData() {

    }

    @Override
    public boolean signOut() {
        model.signOut();
        view.signOutFromGoogle();
        view.goLoginActivity();
        return true;
    }
}
