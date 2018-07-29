package com.bsm.mobile.domain.wizard.badge.model;

import com.bsm.mobile.legacy.model.User;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadgeInfoView {

    /**
     * Badge information retrieved from remote database
     */
    private BadgeInfo badgeInfo;

    /**
     * Users awarded with a badge of given type
     */
    private List<User> awardedUsers;
}
