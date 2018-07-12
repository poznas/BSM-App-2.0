package com.bsm.mobile.home;

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

import static com.bsm.mobile.Constants.*;
import static com.bsm.mobile.home.HomeActivityMVP.*;

@AllArgsConstructor
public class HomeModel implements Model {

    private final IUserAuthService userAuthService;
    private final IUserRepository userRepository;
    private final INotificationService notificationService;
    private final IUserPrivilegeRepository privilegeRepository;
    private final IScoreRepository scoreRepository;
    private final IPendingReportsService pendingReportsService;

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
    public void signOut() {
        userAuthService.signOut();
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
    public Single<Long> getProfessorPendingReportsNumber() {
        return pendingReportsService.getProfessorPendingReportsNumber();
    }
}
