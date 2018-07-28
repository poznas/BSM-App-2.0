package com.bsm.mobile.professor.admin.user;

import com.bsm.mobile.common.MultiSubscriber;
import com.bsm.mobile.common.SnackMessage;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.legacy.model.User;

import io.reactivex.Single;

public interface EditUserActivityMVP {

    interface View extends SnackMessage {

        void displayUserData(User user);

        void showMessage(String message);

        void finish();
    }

    interface Presenter extends Tagable, MultiSubscriber {

        void attachView(View view);

        void handleBundle(User user);

        void onMakeJudgeClick(User user);

        void onUpdateWizardClick(User user);
    }

    interface  Model {

        Single<Boolean> updateUser(User user);
    }
}
