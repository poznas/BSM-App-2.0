package com.bsm.mobile.common.utils;

import com.bsm.mobile.common.resource.TeamResources;
import com.bsm.mobile.legacy.model.User;
import com.bsm.mobile.legacy.model.User.UserBuilder;

import java.util.Arrays;
import java.util.List;

import static com.bsm.mobile.common.resource.Constants.GENDER_FEMALE;
import static com.bsm.mobile.common.resource.Constants.GENDER_MALE;
import static com.bsm.mobile.common.resource.Constants.LABEL_JUDGE;
import static com.bsm.mobile.common.resource.Constants.LABEL_PROFESSOR;
import static com.bsm.mobile.common.resource.Constants.LABEL_WIZARD;

public class UserDataValidator {

    public static final List<String> VALID_GENDERS
            = Arrays.asList(GENDER_MALE, GENDER_FEMALE);
    public static final List<String> VALID_LABELS
            = Arrays.asList(LABEL_JUDGE, LABEL_WIZARD, LABEL_PROFESSOR);
    public static final List<String> VALID_TEAMS
            = TeamResources.IDENTIFIERS;


    public static boolean validGender(User user){
        return VALID_GENDERS.contains(user.getGender());
    }

    public static boolean validLabel(User user){
        return VALID_LABELS.contains(user.getLabel());
    }

    public static boolean validTeam(User user){
        return VALID_TEAMS.contains(user.getTeam());
    }

    public static boolean validDisplayName(User user){
        return user.getDisplayName() != null
                && user.getDisplayName().length() > 3
                && user.getDisplayName().length() <= 8;
    }

    public static User getValidData(User user, boolean forDetailsBranch) {
        UserBuilder builder =
                user.getLabel().equals(LABEL_JUDGE) ?
                        User.builder()
                                .label(LABEL_JUDGE) :
                        User.builder()
                                .facebook(user.getFacebook())
                                .gender(validGender(user) ? user.getGender() : null)
                                .label(validLabel(user) ? user.getLabel() : null)
                                .team(validTeam(user) ? user.getTeam() : null);

        return (
                forDetailsBranch ?
                        builder :
                        builder
                                .displayName(user.getDisplayName())
                                .photoUrl(user.getPhotoUrl())
                                .email(user.getEmail())
                                .id(user.getId())
        ).build();
    }
}
