package com.bsm.mobile.core;


import android.support.design.widget.Snackbar;
import android.view.View;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;


public interface SnackMessage {

    default void popMessage(View rootView, String message){
        Snackbar.make(rootView, message, LENGTH_SHORT).show();
    }
}
