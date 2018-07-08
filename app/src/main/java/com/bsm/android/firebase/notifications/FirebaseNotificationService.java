package com.bsm.android.firebase.notifications;

import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseNotificationService implements INotificationService{

    @Override
    public void subscribeToTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }

    @Override
    public void unsubscribeFromTopic(String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }
}
