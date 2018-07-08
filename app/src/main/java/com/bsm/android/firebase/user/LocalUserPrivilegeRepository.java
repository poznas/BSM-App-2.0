package com.bsm.android.firebase.user;

import com.bsm.android.R;
import com.bsm.android.model.Privilege;
import com.bsm.android.model.User;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;

import static com.bsm.android.Constants.*;

public class LocalUserPrivilegeRepository implements IUserPrivilegeRepository {

    @Override
    public Observable<List<Privilege>> getUserPrivileges(User user) {

        String userLabel = user.getLabel();
        List<Privilege> privileges = new LinkedList<>();

        if( userLabel.equals(LABEL_PROFESSOR)){
            privileges.add(Privilege.builder()
                    .iconId(R.mipmap.icon_small_mc).brand(BRAND_MAIN_COMPETITION).build());
            privileges.add(Privilege.builder()
                    .iconId(R.mipmap.icon_small_medal).brand(BRAND_MEDAL).build());
            privileges.add(Privilege.builder()
                    .iconId(R.mipmap.icon_small_prof).brand(BRAND_PROF_RATE).checkIfContain(true).pendingReports(REPORTS_LOADING).build());
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
                    .iconId(R.mipmap.icon_small_calendar).brand(BRAND_CALENDAR).build());
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

        return Observable.just(privileges);
    }
}
