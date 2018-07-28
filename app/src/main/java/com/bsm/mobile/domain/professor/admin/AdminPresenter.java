package com.bsm.mobile.domain.professor.admin;

import com.bsm.mobile.legacy.model.User;

import java.util.LinkedList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;

import static com.bsm.mobile.common.resource.Message.ADMIN_DELETE_USER_FAILURE;
import static com.bsm.mobile.common.resource.Message.ADMIN_DELETE_USER_SUCCESS;
import static com.bsm.mobile.common.resource.Message.ADMIN_REPORT_DISABLED;
import static com.bsm.mobile.common.resource.Message.ADMIN_REPORT_ENABLED;
import static com.bsm.mobile.common.resource.Message.ADMIN_REPORT_LOCK_UPDATE_ERROR;
import static com.bsm.mobile.domain.professor.admin.AdminActivityMVP.Model;
import static com.bsm.mobile.domain.professor.admin.AdminActivityMVP.Presenter;
import static com.bsm.mobile.domain.professor.admin.AdminActivityMVP.View;

@RequiredArgsConstructor
public class AdminPresenter implements Presenter {

    private View view;
    private final Model model;

    private LinkedList<Disposable> subscriptions;

    @Override
    public void attachView(View view) {
        this.view = view;
        subscriptions = new LinkedList<>();
        view.setReportLockProgress(true);
        view.setUserListProgress(true);
    }

    @Override
    public void unsubscribe() {
        clearSubscriptions(subscriptions);
    }

    @Override
    public void subscribeForData() {
        subscribeForReportLockState();
        subscribeForUsers();
    }

    private void subscribeForReportLockState() {
        subscriptions.add(
                model.getReportLockState()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnEach(unlocked -> view.setReportLockProgress(false))
                        .subscribe(view::updateReportLock));
    }

    private void subscribeForUsers() {
        subscriptions.add(
                model.getUserList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnEach(users -> view.setUserListProgress(false))
                        .subscribe(view::updateUsers));
    }

    @Override
    public void handleSwitchChange(boolean unlocked) {
        subscriptions.add(
                model.setReportLockState(unlocked)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(updateSuccess -> view.showMessage(
                                updateSuccess ?
                                        unlocked ?
                                                ADMIN_REPORT_ENABLED
                                                : ADMIN_REPORT_DISABLED
                                        : ADMIN_REPORT_LOCK_UPDATE_ERROR

                        )));
    }

    @Override
    public void deleteUser(User user) {
        subscriptions.add(
                model.deleteUser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(success -> view.showMessage(success ?
                                ADMIN_DELETE_USER_SUCCESS
                                : ADMIN_DELETE_USER_FAILURE)));
    }
/*
    @Override
    public void updateUser(User user) {
        subscriptions.add(
                model.updateUser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(success -> view.showMessage(success ?
                                ADMIN_UPDATE_USER_SUCCESS
                                : ADMIN_UPDATE_USER_FAILURE)));
    }
*/
}
