package com.bsm.mobile.domain.points;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bsm.mobile.legacy.domain.points.details.bet.BetResultDisplayActivity;
import com.bsm.mobile.legacy.domain.points.details.mc.MCResultDisplayActivity;
import com.bsm.mobile.legacy.domain.points.details.medal.MedalResultDisplayActivity;
import com.bsm.mobile.legacy.domain.points.details.sm.SMResultDisplayActivity;
import com.bsm.mobile.legacy.domain.points.details.sm.post.SMPostResultDisplayActivity;
import com.bsm.mobile.legacy.model.PointsInfo;

import java.util.Date;

import static com.bsm.mobile.common.resource.Constants.KEY_DATE;
import static com.bsm.mobile.common.resource.Constants.KEY_INFO;
import static com.bsm.mobile.common.resource.Constants.KEY_LOSER;
import static com.bsm.mobile.common.resource.Constants.KEY_MC_NAME;
import static com.bsm.mobile.common.resource.Constants.KEY_POINTS;
import static com.bsm.mobile.common.resource.Constants.KEY_REPORT_ID;
import static com.bsm.mobile.common.resource.Constants.KEY_TEAM;
import static com.bsm.mobile.common.resource.Constants.KEY_TIME;
import static com.bsm.mobile.common.resource.Constants.KEY_WINNER;
import static com.bsm.mobile.common.resource.Constants.LABEL_POINTS_BET;
import static com.bsm.mobile.common.resource.Constants.LABEL_POINTS_MAIN_COMPETITION;
import static com.bsm.mobile.common.resource.Constants.LABEL_POINTS_MEDAL;
import static com.bsm.mobile.common.resource.Constants.LABEL_POINTS_SIDE_MISSION;
import static com.bsm.mobile.common.resource.Constants.dateFormat;
import static com.bsm.mobile.common.resource.Constants.timeFormat;

@SuppressLint("SimpleDateFormat")
public class PointsIntentFactory {

    public static Intent getDisplayDetailsIntent(Context context, PointsInfo pointsInfo) {



        Bundle basicExtras = new Bundle();
        basicExtras.putLong(KEY_POINTS, pointsInfo.getPoints());
        basicExtras.putString(KEY_TEAM, pointsInfo.getTeam());
        basicExtras.putString(KEY_TIME, timeFormat.format(new Date(pointsInfo.getTimestamp())));
        basicExtras.putString(KEY_DATE, dateFormat.format(new Date(pointsInfo.getTimestamp())));

        switch (pointsInfo.getLabel()){

            case LABEL_POINTS_MAIN_COMPETITION:
                return new Intent(context, MCResultDisplayActivity.class)
                        .putExtras(basicExtras)
                        .putExtra(KEY_MC_NAME, pointsInfo.getName())
                        .putExtra(KEY_INFO, pointsInfo.getInfo());

            case LABEL_POINTS_BET:
                return new Intent(context, BetResultDisplayActivity.class)
                        .putExtras(basicExtras)
                        .putExtra(KEY_INFO, pointsInfo.getInfo())
                        .putExtra(KEY_LOSER, pointsInfo.getLosser())
                        .putExtra(KEY_WINNER, pointsInfo.getWinner());

            case LABEL_POINTS_MEDAL:
                return new Intent(context, MedalResultDisplayActivity.class)
                        .putExtras(basicExtras)
                        .putExtra(KEY_INFO, pointsInfo.getInfo());

            case LABEL_POINTS_SIDE_MISSION:
                return pointsInfo.getIsPost() ?
                        new Intent(context, SMPostResultDisplayActivity.class)
                                .putExtras(basicExtras)
                                .putExtra(KEY_REPORT_ID, pointsInfo.getId()) :
                        new Intent(context, SMResultDisplayActivity.class)
                                .putExtras(basicExtras)
                                .putExtra(KEY_REPORT_ID, pointsInfo.getId());

            default:
                return null;
        }
    }
}
