package com.bsm.mobile.common;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import lombok.Setter;


public class SimpleAlertDialog extends AlertDialog.Builder {

    private static final String MESSAGE_DIALOG_YES = "Tak";
    private static final String MESSAGE_DIALOG_NO = "Nie";

    private OnClickListener onClickListener;

    public SimpleAlertDialog(Context context){
        super(context);

        onClickListener = new OnClickListener();

        setPositiveButton(MESSAGE_DIALOG_YES, onClickListener);
        setNegativeButton(MESSAGE_DIALOG_NO, onClickListener);
    }

    public SimpleAlertDialog setMessage(String message){
        super.setMessage(message);

        return this;
    }

    public SimpleAlertDialog setOnPositiveClick(PositiveClicker clicker){
        onClickListener.setClicker(clicker);

        return this;
    }

    @Setter
    class OnClickListener implements DialogInterface.OnClickListener{

        PositiveClicker clicker;

        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    clicker.onPositiveClick();
                    break;
                default:
                    break;
            }
        }
    }

    public interface PositiveClicker{
        void onPositiveClick();
    }
}
