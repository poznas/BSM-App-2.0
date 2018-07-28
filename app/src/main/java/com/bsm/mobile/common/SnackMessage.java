package com.bsm.mobile.common;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;


public interface SnackMessage {

    default void popMessage(View rootView, String message){
        Snackbar.make(rootView, message, LENGTH_SHORT).show();
    }

    default void toastMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
