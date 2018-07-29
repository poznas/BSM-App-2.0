package com.bsm.mobile.backend.score;

import com.bsm.mobile.backend.score.points.FirebasePointsRepository;
import com.bsm.mobile.backend.score.points.IPointsRepository;
import com.bsm.mobile.backend.score.points.IPointsService;
import com.bsm.mobile.backend.score.points.PointsService;
import com.bsm.mobile.backend.score.points.badge.FirebaseBadgeInfoRepository;
import com.bsm.mobile.backend.score.points.badge.FirebaseBadgePointsRepository;
import com.bsm.mobile.backend.score.points.badge.IBadgeInfoRepository;
import com.bsm.mobile.backend.score.points.badge.IBadgePointsRepository;
import com.bsm.mobile.backend.score.points.bet.FirebaseBetPointsRepository;
import com.bsm.mobile.backend.score.points.bet.IBetPointsRepository;
import com.bsm.mobile.backend.score.points.mc.FirebaseMainCompetitionPointsRepository;
import com.bsm.mobile.backend.score.points.mc.IMainCompetitionPointsRepository;
import com.bsm.mobile.backend.score.points.medal.FirebaseMedalPointsRepository;
import com.bsm.mobile.backend.score.points.medal.IMedalPointsRepository;
import com.bsm.mobile.backend.score.points.sm.FirebaseSideMissionPointsRepository;
import com.bsm.mobile.backend.score.points.sm.ISideMissionPointsRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ScoreBackendModule {

    @Singleton
    @Provides
    public IScoreRepository scoreRepository(){
        return new FirebaseScoreRepository();
    }

    @Singleton
    @Provides
    public IPointsService pointsService(
            IPointsRepository pointsRepository,
            IBetPointsRepository betPointsRepository,
            IMainCompetitionPointsRepository mainCompetitionPointsRepository,
            IMedalPointsRepository medalPointsRepository,
            ISideMissionPointsRepository sideMissionPointsRepository
    ){
        return new PointsService(
                pointsRepository,
                betPointsRepository,
                mainCompetitionPointsRepository,
                medalPointsRepository,
                sideMissionPointsRepository
        );
    }

    @Singleton
    @Provides
    public IPointsRepository pointsRepository(){
        return new FirebasePointsRepository();
    }

    @Singleton
    @Provides
    public IBetPointsRepository betPointsRepository(){
        return new FirebaseBetPointsRepository();
    }

    @Singleton
    @Provides
    public IMainCompetitionPointsRepository mainCompetitionPointsRepository(){
        return new FirebaseMainCompetitionPointsRepository();
    }

    @Singleton
    @Provides
    public IMedalPointsRepository medalPointsRepository(){
        return new FirebaseMedalPointsRepository();
    }

    @Singleton
    @Provides
    public ISideMissionPointsRepository sideMissionPointsRepository(){
        return new FirebaseSideMissionPointsRepository();
    }

    @Singleton
    @Provides
    public IBadgePointsRepository badgePointsRepository(){
        return new FirebaseBadgePointsRepository();
    }

    @Singleton
    @Provides
    public IBadgeInfoRepository badgeInfoRepository(){
        return new FirebaseBadgeInfoRepository();
    }

}
