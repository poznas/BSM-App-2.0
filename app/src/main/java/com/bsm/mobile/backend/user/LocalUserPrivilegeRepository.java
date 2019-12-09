package com.bsm.mobile.backend.user;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.Privilege;
import com.bsm.mobile.legacy.model.User;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;

import static com.bsm.mobile.common.resource.Constants.BRAND_BET;
import static com.bsm.mobile.common.resource.Constants.BRAND_MAIN_COMPETITION;
import static com.bsm.mobile.common.resource.Constants.BRAND_MC_INFO;
import static com.bsm.mobile.common.resource.Constants.BRAND_MEDAL;
import static com.bsm.mobile.common.resource.Constants.BRAND_SM_INFO;
import static com.bsm.mobile.common.resource.Constants.LABEL_PROFESSOR;

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
                    .iconId(R.mipmap.icon_small_bet).brand(BRAND_BET).build());
        }

        privileges.add(Privilege.builder()
                .iconId(R.mipmap.icon_small_sm_info).brand(BRAND_SM_INFO).build());
        privileges.add(Privilege.builder()
                .iconId(R.mipmap.icon_small_mc_info).brand(BRAND_MC_INFO).build());

        return Observable.just(privileges);
    }
}
