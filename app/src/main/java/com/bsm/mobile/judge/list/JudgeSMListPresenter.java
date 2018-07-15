package com.bsm.mobile.judge.list;

import java.util.LinkedList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.bsm.mobile.judge.list.JudgeSMListActivityMVP.*;

public class JudgeSMListPresenter implements Presenter {

    private View view;
    private Model model;

    private LinkedList<Disposable> subscriptions;

    public JudgeSMListPresenter(Model model) {
        this.model = model;
    }

    @Override
    public void attachView(View view) {
        this.view = view;
        subscriptions = new LinkedList<>();
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
