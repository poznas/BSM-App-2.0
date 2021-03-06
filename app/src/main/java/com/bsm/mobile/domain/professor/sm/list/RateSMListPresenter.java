package com.bsm.mobile.domain.professor.sm.list;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;

import static com.bsm.mobile.domain.professor.sm.list.RateSMListActivityMVP.Model;
import static com.bsm.mobile.domain.professor.sm.list.RateSMListActivityMVP.Presenter;
import static com.bsm.mobile.domain.professor.sm.list.RateSMListActivityMVP.View;

@RequiredArgsConstructor
public class RateSMListPresenter implements Presenter {

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
                model.getRequireProfessorRateReports()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnEach(reports -> view.hideProgress())
                        .subscribe(view::updateRequireProfessorRateReports)
        );
    }
}
