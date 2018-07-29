package com.bsm.mobile.domain.wizard.badge;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;

import static com.bsm.mobile.domain.wizard.badge.BadgeInfoActivityMVP.Model;
import static com.bsm.mobile.domain.wizard.badge.BadgeInfoActivityMVP.Presenter;
import static com.bsm.mobile.domain.wizard.badge.BadgeInfoActivityMVP.View;

@RequiredArgsConstructor
public class BadgeInfoPresenter implements Presenter {

    private View view;
    private final Model model;

    private CompositeDisposable subscriptions;

    @Override
    public void attachView(View view) {
        this.view = view;
        subscriptions = new CompositeDisposable();
        this.view.showProgress();
    }

    @Override
    public void unsubscribe() {
        clearSubscriptions(subscriptions);
    }

    @Override
    public void subscribeForData() {
        subscriptions.add(
                model.getBadgeInfoList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnEach(list -> view.hideProgress())
                        .subscribe(view::updateBadgeInfoList)
        );
    }
}
