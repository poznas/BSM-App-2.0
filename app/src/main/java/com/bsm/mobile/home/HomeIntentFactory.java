package com.bsm.mobile.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.bsm.mobile.judge.list.JudgeSMListActivity;
import com.bsm.mobile.legacy.module.info.mc.MainCompetitionInfoActivity;
import com.bsm.mobile.legacy.module.info.sm.SideMissionsInfoActivity;
import com.bsm.mobile.legacy.module.professor.bet.AddBetActivity;
import com.bsm.mobile.legacy.module.professor.mc.AddMCActivity;
import com.bsm.mobile.legacy.module.professor.medal.AddMedalActivity;
import com.bsm.mobile.legacy.module.wizard.list.WizardsActivity;
import com.bsm.mobile.legacy.module.wizard.sm.list.AddSMListActivity;
import com.bsm.mobile.legacy.module.zongler.ZonglerActivity;
import com.bsm.mobile.points.list.PointsListActivity;
import com.bsm.mobile.professor.admin.AdminActivity;
import com.bsm.mobile.professor.sm.list.RateSMListActivity;

import java.util.HashMap;

import static com.bsm.mobile.Constants.BRAND_ADMIN;
import static com.bsm.mobile.Constants.BRAND_BET;
import static com.bsm.mobile.Constants.BRAND_JUDGE;
import static com.bsm.mobile.Constants.BRAND_MAIN_COMPETITION;
import static com.bsm.mobile.Constants.BRAND_MC_INFO;
import static com.bsm.mobile.Constants.BRAND_MEDAL;
import static com.bsm.mobile.Constants.BRAND_PROF_RATE;
import static com.bsm.mobile.Constants.BRAND_REPORT;
import static com.bsm.mobile.Constants.BRAND_SM_INFO;
import static com.bsm.mobile.Constants.BRAND_TUTORIAL;
import static com.bsm.mobile.Constants.BRAND_WIZARDS;
import static com.bsm.mobile.Constants.BRAND_ZONGLER;
import static com.bsm.mobile.Constants.KEY_TEAM;
import static com.bsm.mobile.Constants.TEAM_CORMEUM;
import static com.bsm.mobile.Constants.TEAM_MUTINIUM;
import static com.bsm.mobile.Constants.TEAM_SENSUM;
import static com.bsm.mobile.Constants.URL_TUTORIAL_FOLDER;

public class HomeIntentFactory {

    public static HashMap<String, Intent> getPrivilegeIntentMap(Context context){
        return new HashMap<String, Intent>(){{
            put(BRAND_WIZARDS, new Intent(context, WizardsActivity.class));
            put(BRAND_SM_INFO, new Intent(context, SideMissionsInfoActivity.class));
            put(BRAND_MC_INFO, new Intent(context, MainCompetitionInfoActivity.class));
            put(BRAND_TUTORIAL, new Intent(Intent.ACTION_VIEW).setData(Uri.parse(URL_TUTORIAL_FOLDER)));
            put(BRAND_MAIN_COMPETITION, new Intent(context, AddMCActivity.class));
            put(BRAND_BET, new Intent(context, AddBetActivity.class));
            put(BRAND_MEDAL, new Intent(context, AddMedalActivity.class));
            put(BRAND_ZONGLER, new Intent(context, ZonglerActivity.class));
            put(BRAND_REPORT, new Intent(context, AddSMListActivity.class));
            put(BRAND_JUDGE, new Intent(context, JudgeSMListActivity.class));
            put(BRAND_PROF_RATE, new Intent(context, RateSMListActivity.class));
            put(BRAND_ADMIN, new Intent(context, AdminActivity.class));
        }};
    }

    public static HashMap<String, Intent> getTeamIntentMap(Context context){
        return new HashMap<String, Intent>(){{
            put(TEAM_CORMEUM, new Intent(context, PointsListActivity.class).putExtra(KEY_TEAM, TEAM_CORMEUM));
            put(TEAM_SENSUM, new Intent(context, PointsListActivity.class).putExtra(KEY_TEAM, TEAM_SENSUM));
            put(TEAM_MUTINIUM, new Intent(context, PointsListActivity.class).putExtra(KEY_TEAM, TEAM_MUTINIUM));
        }};
    }
}
