package com.bsm.mobile.professor.admin;

import com.bsm.mobile.common.MultiSubscriber;
import com.bsm.mobile.common.SnackMessage;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.legacy.model.User;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface AdminActivityMVP {

    interface View extends SnackMessage {

        void setReportLockSwitchListener();

        void setReportLockProgress(boolean loading);

        void setUserListProgress(boolean loading);

        void showMessage(String message);

        void updateUsers(List<User> users);

        void updateReportLock(boolean unlocked);
    }

    interface Presenter extends Tagable, MultiSubscriber {

        void attachView(View view);

        void unsubscribe();

        void subscribeForData();

        void deleteUser(User user);

        void updateUser(User user);

        void insertUser(User user);

        void handleSwitchChange(boolean isChecked);
    }

    interface Model {

        Single<Boolean> deleteUser(User user);

        Single<Boolean> updateUser(User user);

        Single<Boolean> insertUser(User user);

        Single<Boolean> setReportLockState(boolean unlocked);

        Observable<Boolean> getReportLockState();

        Observable<List<User>> getUserList();
    }
}
