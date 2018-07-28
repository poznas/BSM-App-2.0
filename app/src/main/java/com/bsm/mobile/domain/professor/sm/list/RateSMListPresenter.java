package com.bsm.mobile.domain.professor.sm.list;

import java.util.LinkedList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;

import static com.bsm.mobile.domain.professor.sm.list.RateSMListActivityMVP.Model;
import static com.bsm.mobile.domain.professor.sm.list.RateSMListActivityMVP.Presenter;
import static com.bsm.mobile.domain.professor.sm.list.RateSMListActivityMVP.View;

@RequiredArgsConstructor
public class RateSMListPresenter implements Presenter {

    private View view;
    private final Model model;

    private LinkedList<Disposable> subscriptions;


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
                model.getRequireProfessorRateReports()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnEach(reports -> view.hideProgress())
                        .subscribe(view::updateRequireProfessorRateReports)
        );
    }
}
