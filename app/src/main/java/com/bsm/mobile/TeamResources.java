package com.bsm.mobile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bsm.mobile.Constants.TEAM_CORMEUM;
import static com.bsm.mobile.Constants.TEAM_MUTINIUM;
import static com.bsm.mobile.Constants.TEAM_SENSUM;

public class TeamResources {

    public static final List<String> IDENTIFIERS =
            Arrays.asList(TEAM_CORMEUM, TEAM_SENSUM, TEAM_MUTINIUM);

    public static final Map<String, String> DISPLAY_NAMES = new HashMap<String, String>(){{
       put(TEAM_CORMEUM, "Cormeum");
       put(TEAM_SENSUM, "Sensum");
       put(TEAM_MUTINIUM, "Mutinium");
    }};

    public static final Map<String, Integer> COLORS = new HashMap<String, Integer>(){{
        put(TEAM_CORMEUM, R.color.red);
        put(TEAM_SENSUM, R.color.blue);
        put(TEAM_MUTINIUM, R.color.green);
    }};

    public static final Map<String, Integer> IMAGES = new HashMap<String, Integer>(){{
        put(TEAM_CORMEUM, R.mipmap.cormeum);
        put(TEAM_SENSUM, R.mipmap.sensum);
        put(TEAM_MUTINIUM, R.mipmap.mutinium);
    }};
}
