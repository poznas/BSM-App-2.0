package com.bsm.mobile.legacy.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.bsm.mobile.R;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.home.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Mlody Danon on 7/20/2017.
 */

public class MessagingService extends FirebaseMessagingService implements Tagable{

    private String mBody;
    private String mTitle;
    private Bitmap largeIcon;

    private static final int ID = 2137;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if( remoteMessage.getNotification() != null ){
            mBody = remoteMessage.getNotification().getBody();
            mTitle = remoteMessage.getNotification().getTitle();
            String mPhotoUrl = remoteMessage.getNotification().getIcon();

            Log.d(getTag(), "New Notification: "+mTitle+" | "+mBody);

            URL url;
            try {
                url = new URL(mPhotoUrl);
                largeIcon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            displayNotification();
        }
    }

    private void displayNotification() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationBuilder
                = new Notification.Builder(this)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.judge_icon)
                .setLargeIcon(largeIcon)
                .setContentTitle(mTitle)
                .setContentText(mBody)
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setVibrate( new long[] { 1000, 1000 } );

        NotificationManager notificationManager
                = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (notificationManager != null) {
            notificationManager.cancelAll();
            notificationManager.notify(ID, notificationBuilder.build());
        }
    }
}