package com.bsm.mobile.points;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bsm.mobile.legacy.model.PointsInfo;
import com.bsm.mobile.legacy.module.calendar.CalendarDaysActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.bsm.mobile.Constants.*;

@SuppressLint("SimpleDateFormat")
public class PointsIntentFactory {


    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd MMMM");
    private static final SimpleDateFormat timeFormat =
            new SimpleDateFormat("HH:mm");

    public static Intent getDisplayDetailsIntent(Context context, PointsInfo pointsInfo) {

        Bundle basicExtras = new Bundle();
        basicExtras.putString(KEY_POINTS, String.valueOf(pointsInfo.getPoints()));
        basicExtras.putString(KEY_TEAM, pointsInfo.getTeam());
        basicExtras.putString(KEY_TIME, timeFormat.format(new Date(pointsInfo.getTimestamp())));
        basicExtras.putString(KEY_DATE, dateFormat.format(new Date(pointsInfo.getTimestamp())));

        switch (pointsInfo.getLabel()){

            case LABEL_POINTS_MAIN_COMPETITION:
                return new Intent(context, CalendarDaysActivity.class) //TODO:
                        .putExtras(basicExtras)
                        .putExtra(KEY_MC_NAME, pointsInfo.getName())
                        .putExtra(KEY_INFO, pointsInfo.getInfo());

            case LABEL_POINTS_BET:
                return new Intent(context, CalendarDaysActivity.class)
                        .putExtras(basicExtras)
                        .putExtra(KEY_INFO, pointsInfo.getInfo())
                        .putExtra(KEY_LOSER, pointsInfo.getLosser())
                        .putExtra(KEY_WINNER, pointsInfo.getWinner());

            case LABEL_POINTS_MEDAL:
                return new Intent(context, CalendarDaysActivity.class)
                        .putExtras(basicExtras)
                        .putExtra(KEY_INFO, pointsInfo.getInfo());

            case LABEL_POINTS_SIDE_MISSION:
                return new Intent(context, CalendarDaysActivity.class)
                        .putExtras(basicExtras)
                        .putExtra(KEY_REPORT_ID, pointsInfo.getId());

            default:
                return null;
        }
    }
}
