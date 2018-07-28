package com.bsm.mobile.common.utils;

import com.bsm.mobile.common.resource.TeamResources;
import com.bsm.mobile.legacy.model.User;

import java.util.Arrays;
import java.util.List;

import static com.bsm.mobile.common.resource.Constants.GENDER_FEMALE;
import static com.bsm.mobile.common.resource.Constants.GENDER_MALE;
import static com.bsm.mobile.common.resource.Constants.LABEL_JUDGE;
import static com.bsm.mobile.common.resource.Constants.LABEL_PROFESSOR;
import static com.bsm.mobile.common.resource.Constants.LABEL_WIZARD;

public class UserDataUtils {

    private static final List<String> validGenders
            = Arrays.asList(GENDER_MALE, GENDER_FEMALE);
    private static final List<String> validLabels
            = Arrays.asList(LABEL_JUDGE, LABEL_WIZARD, LABEL_PROFESSOR);
    private static final List<String> validTeams
            = TeamResources.IDENTIFIERS;


    public static boolean validGender(User user){
        return validGenders.contains(user.getGender());
    }

    public static boolean validLabel(User user){
        return validLabels.contains(user.getLabel());
    }

    public static boolean validTeam(User user){
        return validTeams.contains(user.getTeam());
    }

    public static String getUserDetailsId(User user){
        return user.getEmail().replaceAll("\\.", "ś");
    }
}
