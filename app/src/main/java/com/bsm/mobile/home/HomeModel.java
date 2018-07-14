package com.bsm.mobile.home;

import android.content.Context;

import com.bsm.mobile.backend.google.GoogleAuthService;
import com.bsm.mobile.backend.notifications.INotificationService;
import com.bsm.mobile.backend.report.IPendingReportsService;
import com.bsm.mobile.backend.score.IScoreRepository;
import com.bsm.mobile.backend.user.IUserAuthService;
import com.bsm.mobile.backend.user.IUserPrivilegeRepository;
import com.bsm.mobile.backend.user.IUserRepository;
import com.bsm.mobile.model.Privilege;
import com.bsm.mobile.model.User;


import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
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
    private IUserRepository userRepository;
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
        return userRepository.getUser(userAuthService.getCurrentUserId());
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
        return scoreRepository.getScoresStream();
    }

    @Override
    public Single<Long> getJudgePendingReportsNumber() {
        return pendingReportsService.getJudgePendingReportsNumber();
    }

    @Override
    public Observable<Long> getProfessorPendingReportsNumber() {
        return pendingReportsService.getProfessorPendingReportsNumber();
    }

    @Override
    public void createGoogleApiClient(View view) {
        googleAuthService = new GoogleAuthService((Context) view);
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
