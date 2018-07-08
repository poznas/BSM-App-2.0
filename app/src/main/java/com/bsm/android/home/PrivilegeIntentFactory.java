package com.bsm.android.home;

import android.content.Context;
import android.content.Intent;

import com.bsm.android.login.LoginActivity;

import java.util.HashMap;

import static com.bsm.android.Constants.*;

public class PrivilegeIntentFactory {

    public static HashMap<String, Intent> getMap(Context context){
        return new HashMap<String, Intent>(){{
            put(BRAND_WIZARDS, new Intent(context, LoginActivity.class));
            put(BRAND_SM_INFO, new Intent(context, LoginActivity.class));
            put(BRAND_MC_INFO, new Intent(context, LoginActivity.class));
            put(BRAND_CALENDAR, new Intent(context, LoginActivity.class));
            put(BRAND_MAIN_COMPETITION, new Intent(context, LoginActivity.class));
            put(BRAND_BET, new Intent(context, LoginActivity.class));
            put(BRAND_MEDAL, new Intent(context, LoginActivity.class));
            put(BRAND_ZONGLER, new Intent(context, LoginActivity.class));
            put(BRAND_REPORT, new Intent(context, LoginActivity.class));
            put(BRAND_JUDGE, new Intent(context, LoginActivity.class));
            put(BRAND_PROF_RATE, new Intent(context, LoginActivity.class));
        }};
    }
}
