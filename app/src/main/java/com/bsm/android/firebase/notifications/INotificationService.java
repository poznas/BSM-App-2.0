package com.bsm.android.firebase.notifications;

public interface INotificationService {


    void subscribeToTopic(String topic);

    void unsubscribeFromTopic(String topic);
}
