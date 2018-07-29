package com.bsm.mobile.domain.wizard.badge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadgeInfo {

    /**
     * Side mission name, badge identifier
     */
    private String sideMissionName;

    /**
     * Side mission image URL
     */
    private String sideMissionImageUrl;

    /**
     * Initial amount of badges available to achieve
     */
    private Long totalAmount;

    /**
     * Number of badges still being available to achieve
     */
    private Long remainingBadges;

    /**
     * Number of rated missions of given type required to achieve a badge
     */
    private Long requiredExecutions;
}
