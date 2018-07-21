package com.bsm.mobile.home;

import android.content.Context;
import android.content.Intent;

import com.bsm.mobile.judge.list.JudgeSMListActivity;
import com.bsm.mobile.legacy.module.calendar.CalendarDaysActivity;
import com.bsm.mobile.legacy.module.info.mc.MainCompetitionInfoActivity;
import com.bsm.mobile.legacy.module.info.sm.SideMissionsInfoActivity;
import com.bsm.mobile.legacy.module.professor.bet.AddBetActivity;
import com.bsm.mobile.legacy.module.professor.mc.AddMCActivity;
import com.bsm.mobile.legacy.module.professor.medal.AddMedalActivity;
import com.bsm.mobile.legacy.module.wizard.list.WizardsActivity;
import com.bsm.mobile.legacy.module.wizard.sm.list.AddSMListActivity;
import com.bsm.mobile.legacy.module.zongler.ZonglerActivity;
import com.bsm.mobile.login.LoginActivity;

import java.util.HashMap;

import static com.bsm.mobile.Constants.*;

public class HomeIntentFactory {

    public static HashMap<String, Intent> getPrivilegeIntentMap(Context context){
        return new HashMap<String, Intent>(){{
            put(BRAND_WIZARDS, new Intent(context, WizardsActivity.class));
            put(BRAND_SM_INFO, new Intent(context, SideMissionsInfoActivity.class));
            put(BRAND_MC_INFO, new Intent(context, MainCompetitionInfoActivity.class));
            put(BRAND_CALENDAR, new Intent(context, CalendarDaysActivity.class));
            put(BRAND_MAIN_COMPETITION, new Intent(context, AddMCActivity.class));
            put(BRAND_BET, new Intent(context, AddBetActivity.class));
            put(BRAND_MEDAL, new Intent(context, AddMedalActivity.class));
            put(BRAND_ZONGLER, new Intent(context, ZonglerActivity.class));
            put(BRAND_REPORT, new Intent(context, AddSMListActivity.class));
            put(BRAND_JUDGE, new Intent(context, JudgeSMListActivity.class));
            put(BRAND_PROF_RATE, new Intent(context, LoginActivity.class));
        }};
    }

    public static HashMap<String, Intent> getTeamIntentMap(Context context){
        return new HashMap<String, Intent>(){{
            put(TEAM_CORMEUM, new Intent(context, LoginActivity.class).putExtra(KEY_TEAM, TEAM_CORMEUM));
            put(TEAM_SENSUM, new Intent(context, LoginActivity.class).putExtra(KEY_TEAM, TEAM_SENSUM));
            put(TEAM_MUTINIUM, new Intent(context, LoginActivity.class).putExtra(KEY_TEAM, TEAM_MUTINIUM));
        }};
    }
}
