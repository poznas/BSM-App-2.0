package com.bsm.mobile.domain.wizard.badge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadgePoints {

    /**
     * Side mission name, badge identifier
     */
    private String sideMissionName;

    /**
     * User name
     */
    private String userName;

    /**
     * User photo URL
     */
    private String userPhotoUrl;

    /**
     * timestamp
     */
    private Long timestamp;

    /**
     * User team
     */
    private String team;

    /**
     * Number of points acquired with badge
     */
    private Long points;

    /**
     * Indicates whether badge is valid in terms of BSM
     */
    private Boolean valid;

    /**
     * badge points identifier
     */
    private String badgeId;
}
