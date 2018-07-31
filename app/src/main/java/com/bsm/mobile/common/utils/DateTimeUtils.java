package com.bsm.mobile.common.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateTimeUtils {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public static String getTime(Long timestamp) {
        return timeFormat.format(new Date(timestamp));
    }

    public static String getDate(Long timestamp) {
        return dateFormat.format(new Date(timestamp));
    }
}
