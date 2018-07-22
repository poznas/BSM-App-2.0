package com.bsm.mobile.common;

import android.content.DialogInterface;

public abstract class SimpleDialogClickListener implements DialogInterface.OnClickListener {

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                onPositiveClick();
                break;
            default:
                break;
        }
    }

    protected abstract void onPositiveClick();
}
