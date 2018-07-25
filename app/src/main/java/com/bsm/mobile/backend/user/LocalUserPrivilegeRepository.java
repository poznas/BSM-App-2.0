package com.bsm.mobile.backend.user;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.Privilege;
import com.bsm.mobile.legacy.model.User;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;

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
import static com.bsm.mobile.Constants.LABEL_JUDGE;
import static com.bsm.mobile.Constants.LABEL_PROFESSOR;
import static com.bsm.mobile.Constants.LABEL_WIZARD;
import static com.bsm.mobile.Constants.REPORTS_LOADING;

public class LocalUserPrivilegeRepository implements IUserPrivilegeRepository {

    @Override
    public Observable<List<Privilege>> getUserPrivileges(User user) {

        String userLabel = user.getLabel();
        List<Privilege> privileges = new LinkedList<>();

        if( userLabel.equals(LABEL_PROFESSOR)){
            privileges.add(Privilege.builder()
                    .iconId(R.mipmap.icon_small_prof).brand(BRAND_PROF_RATE).checkIfContain(true).pendingReports(REPORTS_LOADING).build());
            privileges.add(Privilege.builder()
                    .iconId(R.mipmap.icon_small_mc).brand(BRAND_MAIN_COMPETITION).build());
            privileges.add(Privilege.builder()
                    .iconId(R.mipmap.icon_small_medal).brand(BRAND_MEDAL).build());
            privileges.add(Privilege.builder()
                    .iconId(R.mipmap.icon_small_bet).brand(BRAND_BET).build());
        }
        if( userLabel.equals(LABEL_JUDGE)){
            privileges.add(Privilege.builder()
                    .iconId(R.mipmap.icon_small_judge).brand(BRAND_JUDGE).checkIfContain(true).pendingReports(REPORTS_LOADING).build());
            privileges.add(Privilege.builder()
                    .iconId(R.mipmap.icon_small_wizards).brand(BRAND_WIZARDS).build());
        }
        if( userLabel.equals(LABEL_PROFESSOR) || userLabel.equals(LABEL_WIZARD) || userLabel.equals(LABEL_JUDGE)){
            privileges.add(Privilege.builder()
                    .iconId(R.mipmap.icon_small_zongler).brand(BRAND_ZONGLER).build());
            privileges.add(Privilege.builder()
                    .iconId(R.mipmap.icon_small_sm_info).brand(BRAND_SM_INFO).build());
        }
        if( userLabel.equals(LABEL_PROFESSOR) || userLabel.equals(LABEL_WIZARD)){
            privileges.add(0, Privilege.builder()
                    .iconId(R.mipmap.icon_small_report).brand(BRAND_REPORT).build());
            privileges.add(Privilege.builder()
                    .iconId(R.mipmap.icon_small_mc_info).brand(BRAND_MC_INFO).build());
        }
        if( userLabel.equals(LABEL_PROFESSOR) || userLabel.equals(LABEL_WIZARD) || userLabel.equals(LABEL_JUDGE)){
            privileges.add(Privilege.builder()
                    .iconId(R.mipmap.icon_small_calendar).brand(BRAND_TUTORIAL).build());
        }
        if (userLabel.equals(LABEL_PROFESSOR)) {
            privileges.add(Privilege.builder()
                    .iconId(R.mipmap.icon_admin).brand(BRAND_ADMIN).build());
        }

        return Observable.just(privileges);
    }
}
