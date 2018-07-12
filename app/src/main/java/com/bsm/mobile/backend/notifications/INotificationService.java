package com.bsm.mobile.backend.notifications;

public interface INotificationService {


    void subscribeToTopic(String topic);

    void unsubscribeFromTopic(String topic);
}
