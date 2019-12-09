package com.bsm.mobile.domain.home;

import android.content.Context;
import android.util.Log;

import com.bsm.mobile.agh.AppDatabase;
import com.bsm.mobile.agh.TeamScore;
import com.bsm.mobile.agh.TeamScoreDao;
import com.bsm.mobile.backend.google.GoogleAuthService;
import com.bsm.mobile.backend.notifications.INotificationService;
import com.bsm.mobile.backend.report.IPendingReportsService;
import com.bsm.mobile.backend.score.IScoreRepository;
import com.bsm.mobile.backend.user.IUserAuthService;
import com.bsm.mobile.backend.user.IUserPrivilegeRepository;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.legacy.model.Privilege;
import com.bsm.mobile.legacy.model.User;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static android.arch.persistence.room.Room.databaseBuilder;
import static com.bsm.mobile.common.resource.Constants.TEAM_CORMEUM;
import static com.bsm.mobile.common.resource.Constants.TEAM_MUTINIUM;
import static com.bsm.mobile.common.resource.Constants.TEAM_SENSUM;
import static com.bsm.mobile.common.resource.Constants.TOPIC_JUDGE;
import static com.bsm.mobile.domain.home.HomeActivityMVP.Model;
import static java.lang.System.currentTimeMillis;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;

@Data
@Builder
@AllArgsConstructor
public class HomeModel implements Model, Tagable {

    private IUserAuthService userAuthService;
    private INotificationService notificationService;
    private IUserPrivilegeRepository privilegeRepository;
    private IScoreRepository scoreRepository;
    private IPendingReportsService pendingReportsService;

    private GoogleAuthService googleAuthService;

    private TeamScoreDao scoreDao;

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
    public Observable<Map<String, Long>> getScores() {

        return localCacheScores()
                .switchIfEmpty(scoreRepository.getScores()
                        .doOnEach(scores -> storeScores(scores.getValue())));
    }

    private Observable<Map<String, Long>> localCacheScores() {
        TeamScore cScore = scoreDao.select(TEAM_CORMEUM);
        TeamScore mScore = scoreDao.select(TEAM_MUTINIUM);
        TeamScore sScore = scoreDao.select(TEAM_SENSUM);

        if (allNotNull(cScore, mScore, sScore)) {
            if (cScore.getTimestamp() > currentTimeMillis() - 1000 * 60) {
                Log.d(getTag(), "Getting cached scores. " + cScore.getTimestamp());
                return Observable.just(
                        ImmutableMap.<String, Long>builder()
                                .put(cScore.getId(), cScore.getScore())
                                .put(mScore.getId(), mScore.getScore())
                                .put(sScore.getId(), sScore.getScore())
                                .build());
            }
        }
        Log.d(getTag(), "No valid cache available. " + cScore.getTimestamp());
        return Observable.empty();
    }

    private void storeScores(Map<String, Long> scores) {
        for (Map.Entry<String, Long> entry : scores.entrySet()) {

            TeamScore newEntry = new TeamScore(entry.getKey(), entry.getValue(), currentTimeMillis());

            TeamScore existing = scoreDao.select(entry.getKey());
            if (existing != null) {
                Log.d(getTag(), "update row" + newEntry);
                scoreDao.update(newEntry);
            } else {
                Log.d(getTag(), "insert row" + newEntry);
                scoreDao.insert(newEntry);
            }
        }
    }

    @Override
    public void attachRoom(Context context) {
        AppDatabase database = databaseBuilder(context, AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();

        scoreDao = database.scoreDao();
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
