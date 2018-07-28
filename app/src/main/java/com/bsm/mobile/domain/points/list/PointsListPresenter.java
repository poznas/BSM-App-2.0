package com.bsm.mobile.domain.points.list;

import android.os.Bundle;

import com.bsm.mobile.legacy.model.PointsInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static com.bsm.mobile.common.resource.Constants.KEY_TEAM;
import static com.bsm.mobile.common.resource.Constants.LABEL_PROFESSOR;
import static com.bsm.mobile.domain.points.list.PointsListActivityMVP.Model;
import static com.bsm.mobile.domain.points.list.PointsListActivityMVP.Presenter;
import static com.bsm.mobile.domain.points.list.PointsListActivityMVP.View;

@Setter
@RequiredArgsConstructor
public class PointsListPresenter implements Presenter {

    private View view;
    private final Model model;

    private String teamId;

    private CompositeDisposable subscriptions;


    @Override
    public void attachView(View view) {
        this.view = view;
        subscriptions = new CompositeDisposable();
        this.view.showProgress();
    }

    @Override
    public void handleBundle(Bundle bundle) {
        teamId = bundle.getString(KEY_TEAM);
        view.setTeamDisplay(teamId);
    }

    @Override
    public void unsubscribe() {
        clearSubscriptions(subscriptions);
    }

    @Override
    public void subscribeForData() {
        subscribeForUserLabel();
        subscribeForPointsRecords();
        subscribeForScore();
    }

    private void subscribeForScore() {
        subscriptions.add(
            model.getScore(teamId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(view::updateScore)
        );
    }

    private void subscribeForPointsRecords() {
        subscriptions.add(
            model.getPointsRecords(teamId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnEach(records -> view.hideProgress())
                    .doAfterTerminate(view::hideProgress)
                    .subscribe(view::updatePointsRecords)
        );
    }

    private void subscribeForUserLabel() {
        subscriptions.add(
          model.getUserLabel()
                  .subscribeOn(Schedulers.io())
                  .observeOn(Schedulers.io())
                  .subscribe(userLabel -> view.setPointsInvalidationPermission(
                          userLabel.equals(LABEL_PROFESSOR)
                  ))
        );
    }

    @Override
    public void invalidatePoints(PointsInfo points) {
        model.invalidatePoints(points);
    }
}
