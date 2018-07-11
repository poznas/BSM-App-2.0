package com.bsm.android.home;

import com.bsm.android.backend.notifications.INotificationService;
import com.bsm.android.backend.score.IScoreRepository;
import com.bsm.android.backend.user.IUserAuthService;
import com.bsm.android.backend.user.IUserPrivilegeRepository;
import com.bsm.android.backend.user.IUserRepository;
import com.bsm.android.model.Privilege;
import com.bsm.android.model.User;


import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import lombok.AllArgsConstructor;

import static com.bsm.android.Constants.*;
import static com.bsm.android.home.HomeActivityMVP.*;

@AllArgsConstructor
public class HomeModel implements Model {

    private IUserAuthService userAuthService;
    private IUserRepository userRepository;
    private INotificationService notificationService;
    private IUserPrivilegeRepository privilegeRepository;
    private IScoreRepository scoreRepository;

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
}
