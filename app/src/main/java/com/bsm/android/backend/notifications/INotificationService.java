package com.bsm.android.backend.notifications;

public interface INotificationService {


    void subscribeToTopic(String topic);

    void unsubscribeFromTopic(String topic);
}
