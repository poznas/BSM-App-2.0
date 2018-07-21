package com.bsm.mobile.backend.score;

import java.util.HashMap;

import io.reactivex.Observable;

public interface IScoreRepository {

    Observable<HashMap<String,Long>> getScores();

    Observable<Long> getScore(String teamId);

}
