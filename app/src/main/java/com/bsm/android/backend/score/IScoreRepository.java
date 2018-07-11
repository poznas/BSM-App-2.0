package com.bsm.android.backend.score;

import java.util.HashMap;

import io.reactivex.Observable;

public interface IScoreRepository {

    Observable<HashMap<String,Long>> getScoresStream();

}
