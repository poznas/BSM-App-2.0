package com.bsm.mobile.home;

import android.content.Context;

import com.bsm.mobile.backend.google.GoogleAuthService;
import com.bsm.mobile.backend.notifications.INotificationService;
import com.bsm.mobile.backend.report.IPendingReportsService;
import com.bsm.mobile.backend.score.IScoreRepository;
import com.bsm.mobile.backend.user.IUserAuthService;
import com.bsm.mobile.backend.user.IUserPrivilegeRepository;
import com.bsm.mobile.legacy.model.Privilege;
import com.bsm.mobile.legacy.model.User;


import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static com.bsm.mobile.Constants.*;
import static com.bsm.mobile.home.HomeActivityMVP.*;

@Data
@Builder
@AllArgsConstructor
public class HomeModel implements Model {

    private IUserAuthService userAuthService;
    private INotificationService notificationService;
    private IUserPrivilegeRepository privilegeRepository;
    private IScoreRepository scoreRepository;
    private IPendingReportsService pendingReportsService;

    private GoogleAuthService googleAuthService;

    @Override
    public Observable<Boolean> getSignInStatus() {
        return userAuthService.isUserSignedIn();
    }

    @Override
    public Observable<User> getUser() {
        return userAuthService.getCurrentUser();
    }

    @Override
    public void makeDeviceSubscribeForJudgeNotifications() {
        notificationService.subscribeToTopic(TOPIC_JUDGE);
    }

    @Override
    public void makeDeviceUnsubscribeFromJudgeNotifications() {
        notificationService.unsubscribeFromTopic(TOPIC_JUDGE);
    }

    @Override
    public Observable<List<Privilege>> getUserPrivileges(User user) {
        return privilegeRepository.getUserPrivileges(user);
    }

    @Override
    public Observable<HashMap<String, Long>> getScores() {
        return scoreRepository.getScores();
    }

    @Override
    public Observable<Long> getJudgePendingReportsNumber() {
        return pendingReportsService.getJudgePendingReportsNumber();
    }

    @Override
    public Observable<Long> getProfessorPendingReportsNumber() {
        return pendingReportsService.getProfessorPendingReportsNumber();
    }

    @Override
    public void createGoogleApiClient(Context context) {
        googleAuthService = new GoogleAuthService(context);
    }

    @Override
    public void deleteNotifications(Context context) {
        notificationService.deleteAllNotifications(context);
    }

    @Override
    public void connectGoogleApi() {
        googleAuthService.connect();
    }

    @Override
    public void disconnectGoogleApi() {
        googleAuthService.disconnect();
    }

    @Override
    public void signOut() {
        userAuthService.signOut();
        googleAuthService.signOut();
    }

}
