package com.bsm.mobile.domain.professor.admin.user;

import android.annotation.SuppressLint;

import com.bsm.mobile.domain.professor.admin.user.EditUserActivityMVP.Presenter;
import com.bsm.mobile.legacy.model.User;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;

import static com.bsm.mobile.common.resource.Constants.LABEL_JUDGE;
import static com.bsm.mobile.common.resource.Message.ADMIN_UPDATE_USER_FAILURE;
import static com.bsm.mobile.common.resource.Message.ADMIN_UPDATE_USER_SUCCESS;
import static com.bsm.mobile.common.resource.Message.INVALID_DISPLAY_NAME;
import static com.bsm.mobile.common.utils.UserDataValidator.validDisplayName;
import static com.bsm.mobile.domain.professor.admin.user.EditUserActivityMVP.Model;
import static com.bsm.mobile.domain.professor.admin.user.EditUserActivityMVP.View;

@SuppressLint("CheckResult")
@RequiredArgsConstructor
public class EditUserPresenter implements Presenter {

    private View view;
    private final Model model;

    @Override
    public void attachView(View view) {
        this.view = view;
    }

    @Override
    public void handleBundle(User user) {
        if( user != null ){
            view.displayUserData(user);
            view.armButtonListeners();
        }
    }


    @Override
    public void onMakeJudgeClick(User user) {
        user.setLabel(LABEL_JUDGE);
        updateUser(user);
    }

    private void updateUser(User user) {
        model.updateUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> view.showMessage(success ?
                        ADMIN_UPDATE_USER_SUCCESS
                        : ADMIN_UPDATE_USER_FAILURE));
        view.finish();
    }

    @Override
    public void onUpdateWizardClick(User user) {
        if(validDisplayName(user))
            updateUser(user);
        else
            view.showMessage(INVALID_DISPLAY_NAME);
    }
}
