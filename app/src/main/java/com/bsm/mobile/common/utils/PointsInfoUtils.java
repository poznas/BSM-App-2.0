package com.bsm.mobile.common.utils;

import com.bsm.mobile.legacy.model.PointsInfo;

import static com.bsm.mobile.common.resource.Constants.LABEL_POINTS_BADGE;
import static com.bsm.mobile.common.resource.Constants.LABEL_POINTS_SIDE_MISSION;

public class PointsInfoUtils {

    public static boolean directWizardPoints(PointsInfo pointsInfo) {
        return LABEL_POINTS_SIDE_MISSION.equals(pointsInfo.getLabel())
                || LABEL_POINTS_BADGE.equals(pointsInfo.getLabel());
    }
}
