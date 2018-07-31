package com.bsm.mobile.common;

import android.content.Intent;
import android.os.Bundle;

import com.bsm.mobile.common.utils.DateTimeUtils;
import com.bsm.mobile.legacy.model.PendingReport;

import lombok.RequiredArgsConstructor;

import static com.bsm.mobile.common.resource.Constants.KEY_DATE;
import static com.bsm.mobile.common.resource.Constants.KEY_REPORT_ID;
import static com.bsm.mobile.common.resource.Constants.KEY_SM_NAME;
import static com.bsm.mobile.common.resource.Constants.KEY_TIME;
import static com.bsm.mobile.common.resource.Constants.KEY_USER_IMAGE_URL;
import static com.bsm.mobile.common.resource.Constants.KEY_USER_NAME;

@RequiredArgsConstructor
public class ReportIntentFactory {

    private final Mapper mapper;

    public Intent get(PendingReport report){
        return mapper.mapIntent(report).putExtras(getCommonExtras(report));
    }

    private static Bundle getCommonExtras(PendingReport report){

        Bundle commonExtras = new Bundle();
        commonExtras.putString(KEY_USER_NAME, report.getPerforming_user());
        commonExtras.putString(KEY_USER_IMAGE_URL, report.getUser_photoUrl());
        commonExtras.putString(KEY_REPORT_ID, report.getRpid());
        commonExtras.putString(KEY_SM_NAME, report.getSm_name());
        commonExtras.putString(KEY_TIME, DateTimeUtils.getTime(report.getTimestamp()));
        commonExtras.putString(KEY_DATE, DateTimeUtils.getDate(report.getTimestamp()));

        return commonExtras;
    }

    public interface Mapper{

        Intent mapIntent(PendingReport report);
    }
}
