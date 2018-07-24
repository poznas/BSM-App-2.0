package com.bsm.mobile.backend.notifications;

import android.content.Context;

public interface INotificationService {


    void subscribeToTopic(String topic);

    void unsubscribeFromTopic(String topic);

    void deleteAllNotifications(Context context);
}
