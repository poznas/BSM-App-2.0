package com.bsm.mobile.domain.judge.list;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;

import static com.bsm.mobile.domain.judge.list.JudgeSMListActivityMVP.Model;
import static com.bsm.mobile.domain.judge.list.JudgeSMListActivityMVP.Presenter;
import static com.bsm.mobile.domain.judge.list.JudgeSMListActivityMVP.View;

@RequiredArgsConstructor
public class JudgeSMListPresenter implements Presenter {

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
                model.getPendingReports()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pendingReports -> {
                                view.updatePendingReports(pendingReports);
                                view.hideProgress();
                            }
                    )
        );
    }
}
