package com.bsm.mobile.domain.points.ranking;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;

import static com.bsm.mobile.domain.points.ranking.SideMissionRankingActivityMVP.Model;
import static com.bsm.mobile.domain.points.ranking.SideMissionRankingActivityMVP.Presenter;
import static com.bsm.mobile.domain.points.ranking.SideMissionRankingActivityMVP.View;

@RequiredArgsConstructor
public class SideMissionRankingPresenter implements Presenter{

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
                model.getRanking()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnEach(list -> view.hideProgress())
                        .subscribe(view::updateRankingList)
        );
    }
}
