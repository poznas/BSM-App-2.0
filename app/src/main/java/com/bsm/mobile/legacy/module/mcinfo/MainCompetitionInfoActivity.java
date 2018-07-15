package com.bsm.mobile.legacy.module.mcinfo;


import com.bsm.mobile.legacy.module.sminfo.SideMissionsInfoActivity;
import com.google.firebase.database.Query;

/**
 * Created by Mlody Danon on 7/25/2017.
 *
 * This class is mostly same as SideMissionsInfoActivity
 * so using its classes and layouts
 *
 * lol watch this
 *  ~ Mlody Danon on 7/15/2018
 */

public class MainCompetitionInfoActivity extends SideMissionsInfoActivity {

    @Override
    protected void initializeInfoRecycler() {
        Query query = mRootRef.child("MainCompetitionsDocs").orderByChild("name");
        proceedInitializingInfoRecycler(query);
    }
}
