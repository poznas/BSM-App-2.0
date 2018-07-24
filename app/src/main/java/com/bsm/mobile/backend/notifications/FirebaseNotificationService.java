package com.bsm.mobile.backend.notifications;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import com.bsm.mobile.common.Tagable;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.content.Context.*;

public class FirebaseNotificationService implements INotificationService, Tagable{

    @Override
    public void subscribeToTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }

    @Override
    public void unsubscribeFromTopic(String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }

    @Override
    public void deleteAllNotifications(Context context) {
        NotificationManager nManager =
                ((NotificationManager) context.getSystemService(NOTIFICATION_SERVICE));
        if (nManager != null) {
            Log.d(getTag(), "delete all bsm notifications from tray");
            nManager.cancelAll();
        }
    }
}
