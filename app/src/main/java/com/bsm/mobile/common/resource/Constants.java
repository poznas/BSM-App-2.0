package com.bsm.mobile.common.resource;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

@SuppressLint("SimpleDateFormat")
public class Constants {

    public static final String KEY_POINTS = "points";
    public static final String KEY_MC_NAME = "name";
    public static final String KEY_SM_NAME = "sm_name";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_IMAGE_URL = "user_image_url";
    public static final String KEY_INFO = "info";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_LOSER = "loser";
    public static final String KEY_WINNER = "winner";
    public static final String KEY_REPORT_ID = "rpid";
    public static final String KEY_TEAM = "team";
    public static final String KEY_USER = "user";

    public static final String TEAM_CORMEUM = "cormeum";
    public static final String TEAM_SENSUM = "sensum";
    public static final String TEAM_MUTINIUM = "mutinium";

    public static final String BRANCH_USERS = "users";
    public static final String BRANCH_USER_DETAILS = "UserDetails";
    public static final String BRANCH_SCORES = "SCORES";
    public static final String BRANCH_REPORT_LOCK = "/reportingEnabled";
    public static final String BRANCH_REPORTS = "Reports";
    public static final String BRANCH_PENDING_REPORTS = "pendingReports";
    public static final String BRANCH_REPORT_RATES = "ReportRates";
    public static final String BRANCH_REQUIRE_PROFESSOR_RATE_REPORTS = "requireProfRate";

    public static final String BRANCH_ALL_POINTS = "AllPoints";
    public static final String BRANCH_BET_POINTS = "BetPoints";
    public static final String BRANCH_MEDAL_POINTS = "SpecialPoints";
    public static final String BRANCH_MAIN_COMPETITION_POINTS = "MainCompetitionPoints";
    public static final String BRANCH_SIDE_MISSION_POINTS = "ReportPoints";

    public static final String FIELD_VALID = "valid";
    public static final String FIELD_TIMESTAMP = "timestamp";

    public static final String GENDER_MALE = "m";
    public static final String GENDER_FEMALE = "f";


    public static final String LABEL_JUDGE = "judge";
    public static final String LABEL_PROFESSOR = "professor";
    public static final String LABEL_WIZARD = "wizzard";

    public static final String LABEL_POINTS_BET = "B";
    public static final String LABEL_POINTS_MEDAL = "S";
    public static final String LABEL_POINTS_SIDE_MISSION = "SM";
    public static final String LABEL_POINTS_MAIN_COMPETITION = "MC";


    public static final String BRAND_REPORT = "Melduj";
    public static final String BRAND_MAIN_COMPETITION = "Konkurencja Główna";
    public static final String BRAND_MEDAL = "Medal";
    public static final String BRAND_PROF_RATE = "Opiniuj";
    public static final String BRAND_BET = "Zakład";
    public static final String BRAND_JUDGE = "Oceń";
    public static final String BRAND_WIZARDS = "Czarodzieje";
    public static final String BRAND_CALENDAR = "Kalendarz";
    public static final String BRAND_ZONGLER = "Żongler";
    public static final String BRAND_SM_INFO = "Misje Poboczne";
    public static final String BRAND_MC_INFO = "Konkurencje Główne";
    public static final String BRAND_ADMIN = "Administracja";
    public static final String BRAND_TUTORIAL = "Tutoriale";

    public static final String URL_TUTORIAL_FOLDER =
            "https://drive.google.com/drive/folders/16wudyPdZ3IToMthfmT8tGqa0YU7Gf8O2";
    public static final String DEFAULT_USER_PHOTO_URL
            = "http://i.kafeteria.pl/0991f9c6631ca79a8bb5b5199b2c39df1fc77dc4";

    public static final int REPORTS_LOADING = -1;
    public static final int REPORTS_NO_PENDING = 0;

    public static final String TOPIC_JUDGE = "reportsToJudge";

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM");
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
}
